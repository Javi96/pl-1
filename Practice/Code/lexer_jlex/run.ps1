rm -ErrorAction SilentlyContinue TinyLexer.java ; java -cp jlex.jar JLex.Main jlexDefinitions.l ; Rename-Item -Path 'jlexDefinitions.l.java' -NewName 'TinyLexer.java'
rm -ErrorAction SilentlyContinue -recurse temp ; javac -d temp -cp .  ./*.java ; java -cp temp lexer_jlex.Main