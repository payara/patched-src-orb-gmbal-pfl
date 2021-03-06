/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.pfl.dynamic.copyobject.impl;

import java.util.Map ;
import java.util.HashMap ;

/** Test out whether FastCache is effective or not.
 */
public class FastCacheTest {
    private final static String[] WORD_LIST = { "American",
	"Brown", "Christian", "Congress", "Doctor", "England", "English",
	"Europe", "French", "God", "I", "John", "Mister",
	"Mr.", "Mrs.", "U.S.", "a", "able", "about", "above", "accept", "access",
	"according", "account", "achieve", "across", "act", "action", "actually", "add", "addition",
	"address", "administration", "advance", "affair", "after", "afternoon", "again", "against", "age",
	"agency", "ago", "agree", "agreement", "aid", "air", "album", "all", "allow",
	"almost", "alone", "along", "already", "also", "although", "always", "am", "among",
	"amount", "an", "analysis", "and", "animal", "announce", "another", "answer", "any",
	"anybody", "anyone", "anything", "anyway", "apparently", "appear", "apply", "approach", "appropriate",
	"are", "area", "aren't", "argument", "arm", "army", "around", "art", "article",
	"articles", "artist", "as", "ask", "asked", "association", "assume", "at", "attend",
	"attention", "attitude", "audience", "authority", "available", "away", "back", "bad", "ball",
	"bank", "bar", "base", "based", "basic", "basis", "be", "bear", "beautiful",
	"because", "become", "bed", "been", "before", "begin", "behind", "being", "believe",
	"below", "best", "better", "between", "beyond", "big", "bill", "bit", "black",
	"board", "boat", "body", "book", "books", "both", "boy", "break", "bring",
	"brother", "buffer", "build", "building", "business", "but", "buy", "by", "call",
	"called", "calls", "came", "can", "can't", "cannot", "captain", "car", "card",
	"care", "carry", "case", "cases", "catch", "cause", "cell", "cent", "center",
	"central", "century", "certain", "certainly", "chance", "change", "changed", "changes", "char",
	"character", "characters", "charge", "check", "child", "children", "choice", "choose", "church",
	"citizen", "city", "claim", "class", "clear", "close", "club", "code", "cold",
	"college", "color", "come", "comes", "coming", "command", "comments", "commission", "committee",
	"common", "community", "company", "compare", "complete", "completely", "computer", "concern", "condition",
	"conference", "consider", "considered", "contact", "contain", "continue", "contribute", "control", "copy",
	"corner", "correct", "cost", "could", "couldn't", "country", "county", "couple", "course",
	"court", "cover", "create", "cs", "culture", "cup", "current", "currently", "cut",
	"dark", "data", "date", "datum", "day", "days", "dead", "deal", "death",
	"decide", "decided", "decision", "defense", "define", "demand", "democratic", "department", "describe",
	"design", "detail", "determine", "develop", "development", "device", "did", "didn't", "die",
	"difference", "different", "difficult", "difficulty", "direct", "direction", "directly", "director", "directory",
	"disagree", "discover", "discuss", "discussion", "disk", "distance", "district", "division", "do",
	"does", "doesn't", "dog", "doing", "dollar", "domain", "don't", "done", "door",
	"doubt", "down", "draw", "drive", "drop", "due", "during", "each", "early",
	"earth", "easy", "eat", "economic", "edge", "effect", "effective", "effort", "either",
	"election", "element", "else", "employee", "end", "enemy", "enjoy", "enough", "enter",
	"entire", "environment", "equipment", "error", "especially", "establish", "even", "evening", "event",
	"ever", "every", "everyone", "everything", "evidence", "exactly", "example", "except", "exist",
	"expect", "experience", "experiment", "explain", "express", "extend", "eye", "face", "facility",
	"fact", "factor", "fail", "faith", "fall", "family", "far", "farm", "fast",
	"father", "fear", "federal", "feed", "feel", "feeling", "few", "field", "fight",
	"figure", "file", "files", "fill", "film", "final", "finally", "find", "fine",
	"finger", "finish", "fire", "firm", "first", "fiscal", "five", "fix", "floor",
	"folks", "follow", "following", "food", "foot", "for", "force", "foreign", "forget",
	"form", "format", "former", "found", "four", "free", "freedom", "friend", "friends",
	"from", "front", "full", "fun", "function", "fund", "future", "game", "games",
	"gas", "general", "generally", "get", "gets", "getting", "girl", "give", "given",
	"glass", "go", "goes", "going", "good", "got", "government", "governor", "great",
	"greater", "ground", "group", "groups", "grow", "growth", "guess", "gun", "guy",
	"had", "hair", "half", "hall", "hand", "hang", "happen", "happened", "hard",
	"hardware", "has", "have", "haven't", "having", "he", "he's", "head", "hear",
	"heard", "heart", "help", "her", "here", "herself", "high", "higher", "hill",
	"him", "himself", "his", "history", "hit", "hold", "home", "hope", "horse",
	"hospital", "hot", "hotel", "hour", "hours", "house", "how", "however", "human",
	"hundred", "husband", "idea", "ideas", "if", "image", "immediately", "importance", "important",
	"improve", "in", "in	", "inch", "include", "including", "increase", "indeed", "indicate",
	"individual", "industrial", "influence", "info", "information", "instance", "instead", "institution", "interact",
	"interest", "interested", "interesting", "interface", "international", "into", "involve", "involved", "is",
	"isn't", "issue", "issues", "it", "it's", "item", "its", "itself", "job",
	"join", "just", "keep", "key", "kid", "kill", "kind", "know", "knowledge",
	"known", "knows", "labor", "lady", "land", "language", "large", "larger", "last",
	"late", "later", "latter", "law", "laws", "lay", "leach", "lead", "leader",
	"learn", "least", "leave", "left", "leg", "legal", "length", "less", "let",
	"letter", "level", "lie", "life", "light", "like", "likely", "limit", "line",
	"lines", "list", "listen", "literature", "little", "live", "local", "long", "longer",
	"look", "looking", "looks", "lose", "loss", "lost", "lot", "lots", "love",
	"low", "machine", "machines", "made", "mail", "main", "maintain", "major", "make",
	"makes", "making", "man", "manager", "manner", "many", "march", "mark", "market",
	"marriage", "marry", "material", "matter", "may", "maybe", "me", "mean", "meaning",
	"means", "measure", "medical", "meet", "meeting", "member", "memory", "men", "mention",
	"mentioned", "merely", "message", "method", "might", "mile", "military", "million", "mind",
	"mine", "minute", "minutes", "mod", "mode", "model", "modern", "moment", "money",
	"month", "months", "moral", "more", "morning", "most", "mother", "mouth", "move",
	"movement", "movie", "much", "music", "must", "my", "myself", "name", "names",
	"nation", "national", "natural", "nature", "near", "nearly", "necessary", "need", "needed",
	"needs", "net", "network", "never", "new", "news", "next", "nice", "night",
	"no", "none", "nor", "normal", "not", "note", "nothing", "now", "number",
	"numbers", "object", "obtain", "occur", "of", "off", "offer", "office", "officer",
	"often", "oh", "oil", "old", "on", "once", "one", "ones", "only",
	"open", "operate", "operation", "opinion", "opinions", "opportunity", "or", "order", "organization",
	"original", "other", "others", "our", "out", "output", "outside", "over", "own",
	"page", "paper", "parent", "park", "part", "particular", "parts", "party", "pass",
	"past", "patient", "pattern", "pay", "peace", "people", "per", "performance", "perhaps",
	"period", "permit", "person", "personal", "phone", "physical", "pick", "picture", "piece",
	"place", "plan", "plane", "plant", "play", "played", "playing", "please", "poem",
	"poet", "point", "points", "police", "policy", "political", "pool", "population", "position",
	"possibility", "possible", "post", "posted", "posting", "postmaster", "power", "practice", "prepare",
	"present", "president", "press", "pressure", "pretty", "prevent", "price", "principle", "private",
	"probably", "problem", "problems", "procedure", "process", "produce", "product", "production", "program",
	"programs", "project", "prove", "provide", "public", "publish", "pull", "purpose", "put",
	"quality", "question", "questions", "quite", "radio", "raise", "range", "rate", "rather",
	"re", "reach", "reaction", "read", "reading", "ready", "real", "realize", "really",
	"reason", "reasonable", "reasons", "receive", "received", "recent", "recently", "recognize", "record",
	"red", "reduce", "region", "relate", "related", "relation", "relationship", "release", "religion",
	"religious", "remain", "remember", "remove", "report", "represent", "request", "require", "required",
	"research", "respect", "response", "responses", "responsibility", "rest", "result", "results", "return",
	"ride", "right", "rights", "rise", "river", "road", "role", "room", "root",
	"rule", "rules", "run", "running", "said", "saint", "sale", "same", "save",
	"saw", "say", "saying", "says", "scene", "school", "science", "screen", "season",
	"second", "secretary", "section", "see", "seek", "seem", "seems", "seen", "select",
	"self", "sell", "send", "sense", "sent", "series", "serious", "serve", "server",
	"service", "set", "settle", "several", "sex", "shall", "she", "ship", "shoot",
	"short", "should", "show", "shows", "side", "sign", "similar", "simple", "simply",
	"since", "single", "sit", "site", "situation", "six", "size", "slowly", "small",
	"smile", "so", "social", "society", "software", "solution", "some", "someone", "something",
	"sometimes", "son", "soon", "sort", "sound", "sounds", "source", "sources", "south",
	"southern", "space", "speak", "special", "specific", "speed", "spend", "spring", "square",
	"stage", "stand", "standard", "start", "started", "state", "statement", "station", "stay",
	"step", "still", "stock", "stop", "story", "street", "strength", "strike", "string",
	"strong", "structure", "student", "students", "study", "stuff", "subject", "success", "such",
	"suddenly", "suffer", "suggest", "summer", "sun", "supply", "support", "suppose", "supposed",
	"sure", "system", "systems", "table", "take", "taken", "takes", "taking", "talk",
	"talking", "tape", "tax", "teacher", "team", "technical", "technique", "tell", "temperature",
	"ten", "term", "test", "text", "than", "that", "that's", "the", "their",
	"them", "themselves", "then", "theory", "there", "there's", "therefore", "these", "they",
	"they're", "thing", "things", "think", "thinking", "third", "this", "those", "though",
	"thought", "three", "through", "throughout", "throw", "thus", "time", "times", "to",
	"today", "together", "told", "too", "took", "tooth", "top", "total", "toward",
	"town", "train", "treatment", "tree", "trial", "tried", "trip", "trouble", "true",
	"truth", "try", "trying", "turn", "two", "type", "under", "understand", "unit",
	"unite", "university", "unless", "until", "up", "upon", "us", "use", "used",
	"useful", "user", "users", "uses", "using", "usually", "value", "various", "vary",
	"version", "very", "via", "view", "visit", "voice", "volume", "vote", "wait",
	"walk", "wall", "want", "wanted", "wants", "war", "was", "wasn't", "watch",
	"water", "way", "we", "weapon", "wear", "week", "weeks", "well", "went",
	"were", "west", "western", "what", "whatever", "when", "where", "whether", "which",
	"while", "white", "who", "whole", "why", "wide", "wife", "will", "willing",
	"win", "window", "wish", "with", "within", "without", "woman", "women", "won't",
	"wonder", "word", "words", "work", "worker", "working", "works", "world", "worth",
	"would", "wouldn't", "write", "writer", "writes", "writing", "written", "wrong", "wrote",
	"year", "years", "yes", "yet", "you", "you're", "young", "your", "yourself" } ;

    private static int[] PATTERN = new int[ 100 ] ;

    static {
	int increment = WORD_LIST.length / PATTERN.length ;
	for (int ctr=0; ctr<PATTERN.length; ctr++) {
            PATTERN[ctr] = ctr * increment;
        }
    }

    private static Map<String,Integer> map = new HashMap<String,Integer>() ;
    private static FastCache<String,Integer> cache = new FastCache<String,Integer>( map ) ;

    private static int NUM_LOOPS = 100 ;

    private static void warmUp() {
	for ( String str : WORD_LIST ) {
            cache.put(str, str.length());
        }

	timeTest( true ) ;
    }

    private static void timeTest( boolean useFastCache ) {
	int sum = 0 ;

	System.out.println( "useFastCache = " + useFastCache ) ;

	if (useFastCache) {
	    for (int ctr1 = 0; ctr1 < NUM_LOOPS; ctr1++) {
                for (int ctr2 = 0; ctr2 < PATTERN.length;
                    ctr2++) {
                    sum += cache.get(WORD_LIST[PATTERN[ctr2]]);
                }
            }
	    long tc = cache.getTotalCount() ;
	    long cc = cache.getCacheCount() ;
	    System.out.println( "Total Calls    = " + tc ) ;
	    System.out.println( "Cache Calls    = " + cc ) ;
	    System.out.println( "Hit Ratio      = " + (cc*100.0)/tc + "%" ) ;
	} else {
	    for (int ctr1 = 0; ctr1 < NUM_LOOPS; ctr1++) {
                for (int ctr2 = 0; ctr2 < PATTERN.length;
                    ctr2++) {
                    sum += map.get(WORD_LIST[PATTERN[ctr2]]);
                }
            }
	}

	System.out.println( sum ) ;
    }

    public static void main( String[] args ) {
	warmUp() ;

	long startTime = System.nanoTime() ;	
	timeTest( false ) ;
	long stopTime = System.nanoTime() ;	
	System.out.println( "Test took " + (stopTime - startTime)/1000 + " microseconds" ) ;

	startTime = System.nanoTime() ;	
	timeTest( true ) ;
	stopTime = System.nanoTime() ;	
	System.out.println( "Test took " + (stopTime - startTime)/1000 + " microseconds" ) ;
    }
}
