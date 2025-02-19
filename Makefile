.PHONY: compile help interpret run tests compact clear

compile:
	@javac HTMLAnalyzer.java

help:
	@echo "Usage: java HTMLAnalyzer <URL>"

interpret:
	java HTMLAnalyzer http://hiring.axreng.com/internship/example1.html
	@echo ""

run:
	@$(MAKE) compile
	@$(MAKE) interpret

tests:
	@echo "Testando example.html"
	@echo ""
	@cat example.html
	@echo ""
	@java HTMLAnalyzer example.html
	@echo ""
	@echo ""
	@echo "Testando example2.html"
	@echo ""
	@cat example2.html
	@echo ""
	@java HTMLAnalyzer example2.html
	@echo ""
	@echo ""
	@echo "Testando example3.html"
	@echo ""
	@cat example3.html
	@echo ""
	@java HTMLAnalyzer example3.html
	@echo ""
	@echo ""
	@echo "Testando example4.html"
	@echo ""
	@cat example4.html
	@echo ""
	@java HTMLAnalyzer example4.html
	@echo ""
	@echo ""
	@echo "Testando example5.html"
	@echo ""
	@cat example5.html
	@echo ""
	@java HTMLAnalyzer example5.html
	@echo ""
	@echo ""
	@echo "Testando example6.html"
	@echo ""
	@cat example6.html
	@echo ""
	@java HTMLAnalyzer example6.html
	@echo ""
	@echo ""
	@echo "Testando example7.html"
	@echo ""
	@cat example7.html
	@echo ""
	@java HTMLAnalyzer example7.html
	@echo ""
	@echo ""
	@echo "Testando com http://hiring.axreng.com/internship/example1.html"
	@echo ""
	@cat example1.html
	@echo ""
	@java HTMLAnalyzer http://hiring.axreng.com/internship/example1.html
	@echo ""

clear:
	rm *.class
	rm *~

download/page/html:
	@wget -nc http://hiring.axreng.com/internship/example1.html

check/for/returning:
	java HTMLAnalyzer http://hiring.axreng.com/internship/example1.html > log.txt
	cat log.txt
compact:
	tar cvf rafael_oliveira_ledo.tar.gz HTMLAnalyzer.java README.md
