/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.pfl.basic.tools.file ;

import java.io.File ;
import java.io.FileInputStream ;
import java.io.InputStreamReader ;
import java.io.BufferedReader ;
import java.io.FileOutputStream ;
import java.io.OutputStreamWriter ;
import java.io.BufferedWriter ;
import java.io.Closeable ;
import java.io.IOException ;

/** File wrapper for text files.  Makes it really easy to open, close, delete, read, and write
 * text files.
 */
public class FileWrapper implements Closeable {
    // java.io is a pain to deal with for text files.  We basically need to
    // create:
    // (for reading) File->FileInputStream->InputStreamReader->BufferedReader
    // (for writing) File->FileOutputStream->OutputStreamWriter->BufferedWriter
    private final File file ;

    private FileInputStream fis ;
    private InputStreamReader isr ;
    private BufferedReader reader ;

    private FileOutputStream fos ;
    private OutputStreamWriter osw ;
    private BufferedWriter writer ;
    
    public enum FileState { CLOSED, OPEN_FOR_READ, OPEN_FOR_WRITE } ;

    private FileState state ;

    /** Create a new FileWrapper for the given File.  Represents the same file in the
     * filesystem as the underlying File object.  getBase() return the FileWrapper
     * for the file system root.
     * @param file File to wrap
     */
    public FileWrapper( final File file ) {
	this.file = file ;

	this.fis = null ;
	this.isr = null ;
	this.reader = null ;

	this.fos = null ;
	this.osw = null ;
	this.writer = null ;
	
	this.state = FileState.CLOSED ;
    }

    public FileWrapper( String str ) {
	this( new File( str ) ) ;
    }

    public FileWrapper( File root, String str ) {
	this( new File( root, str ) ) ;
    }

    public boolean canWrite() {
	return file.canWrite() ;
    }

    @Override
    public String toString() {
	return file.toString() ;
    }

    /** Returns true if either this FileWrapper does not exist,
     * or if the lastModificationTime of this FileWrapper is earlier
     * than that of fw.
     */
    boolean isYoungerThan( FileWrapper fw ) {
	if (file.exists()) {
	    return file.lastModified() < fw.file.lastModified() ;
	}

	return true ;
    }

    public void delete() {
	file.delete() ;
    }

    public String getName() {
	return file.getName() ;
    }

    public String getAbsoluteName() {
	return file.getAbsolutePath() ;
    }

    public byte[] readAll() throws IOException {
        final long length = file.length() ;
        if (length > Integer.MAX_VALUE) {
            throw new IOException( "file too large for readAll" ) ;
        }

        final byte[] result = new byte[(int)length] ;
        final FileInputStream is = new FileInputStream( file ) ;

        try {
            int offset = 0 ;
            int clen = (int)length ;
            int actual = 0 ;
            while (clen > 0) {
                actual = is.read( result, offset, clen ) ;
                offset += actual ;
                clen -= actual ;
            }

            return result ;
        } finally {
            is.close() ;
        }
    }

    public void writeAll( byte[] data ) throws IOException {
        final FileOutputStream os = new FileOutputStream( file ) ;
        try {
            os.write( data ) ;
        } finally {
            os.close() ;
        }
    }

    /** Read the next line from the text file.
     * File state must be FileState OPEN_FOR_READ.
     * Returns null when at the end of file.
     * @return The String just read.
     * @throws IOException for IO errors.
     */
    public String readLine() throws IOException {
	if (state != FileState.OPEN_FOR_READ) {
	    throw new IOException( file + " is not open for reading" ) ;
        }
	
	return reader.readLine() ;
    }

    /** Write the line to the end of the file, including a newline.
     * File state must be FileState OPEN_FOR_WRITE.
     * @param line The line to write.
     * @throws IOException for IO errors.
     */
    public void writeLine( final String line ) throws IOException {
	if (state != FileState.OPEN_FOR_WRITE) {
            throw new IOException(file + " is not open for writing");
        }
	
	writer.write( line, 0, line.length() ) ;
	writer.newLine() ;
    }

    /** Close the file, and set its state to CLOSED.
     * This method does not throw any exceptions.
     */
    @Override
    public void close() {
	try {
	    // Ignore if already closed
	    if (state == FileState.OPEN_FOR_READ) {
		reader.close() ;
		isr.close() ;
		fis.close() ;
	    } else if (state == FileState.OPEN_FOR_WRITE) {
		writer.close() ;
		osw.close() ;
		fos.close() ;
	    }
	    state = FileState.CLOSED ;
	} catch (IOException exc) {
	    // Ignore stupid close IOException
	}
    }

    public enum OpenMode { READ, WRITE, WRITE_EMPTY } ;

    /** Open the (text) file for I/O.  There are two modes:
     * <ul>
     * <li>READ.  In this mode, the file is prepared for reading,
     * starting from the beginning.
     * end-of-file at the time the file is opened.
     * <li>WRITE.  In this mode, the file is prepared for writing,
     * starting at the end of the file.
     * </ul>
     * @param mode READ or WRITE mode.
     * @throws IOException for IO exceptions.
     */
    public void open( final OpenMode mode ) throws IOException {
	if (state==FileState.CLOSED) {
	    if (mode == OpenMode.READ) {
		fis = new FileInputStream( file ) ;
		isr = new InputStreamReader( fis ) ;
		reader = new BufferedReader( isr ) ;
		state = FileState.OPEN_FOR_READ ;
	    } else {
		fos = new FileOutputStream( file, mode==OpenMode.WRITE ) ;
		osw = new OutputStreamWriter( fos ) ;
		writer = new BufferedWriter( osw ) ;
		state = FileState.OPEN_FOR_WRITE ;
	    }
	} else if (state==FileState.OPEN_FOR_READ) {
	    if (mode != OpenMode.READ) {
                throw new IOException(file
                    + " is already open for reading, cannot open for writing");
            }
	} else {
	    // state is OPEN_FOR_WRITE
	    if (mode != OpenMode.WRITE) {
                throw new IOException(file
                    + " is already open for writing, cannot open for reading");
            }
	}
    }

    public FileState getFileState() {
	return state ;
    }

    /** Copy this file to target using buffer to hold data.
     * Does not assume we are using text files.
     * @param target The FileWrapper to copy data to.
     * @param buffer The buffer to use for copying the files.
     * @throws IOException for IO exceptions.
     */
    public void copyTo( FileWrapper target, byte[] buffer ) throws IOException {
	FileInputStream is = null ;
	FileOutputStream os = null ;

	try {
	    is = new FileInputStream( this.file ) ;
	    os = new FileOutputStream( target.file ) ;
	    int dataRead = is.read( buffer ) ;
	    while (dataRead > 0) {
		os.write( buffer, 0, dataRead ) ;
		dataRead = is.read( buffer ) ;
	    }
	} finally {
	    if (is != null) {
                is.close();
            }
	    if (os != null) {
                os.close();
            }
	}
    }
}
