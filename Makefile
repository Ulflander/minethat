
CURR_ROOT=`pwd`
U_NAME=`uname`
CONF_ROOT=$(CURR_ROOT)/conf/

NODE_SERVICE = web-server
NODE_SOURCE = $(NODE_SERVICE)/src
NODE_MODULES = $(NODE_SERVICE)/node_modules
GCLOSURE = lib/gclosure-compiler.jar
JAVA_OPTS = "-Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=256m"

# Show makefile usage
help:
	@echo "\nWELCOME IN MINETHAT PROJECT\n"
	@awk 'BEGIN{print "Makefile usage:\n"};/^[^#[:space:]\.].*:/&&$$0!~/=/{split($$0,t,":");printf("%8s %-16s %s\n","make",t[1],x);x=""};/^#/{gsub(/^# /,"");x=$$0;if(x!="")x="- "x};END{printf "\n"}' Makefile


# Initialize java services install (local maven repos)
init:
	@cd java-apps && mvn install:install-file -Dfile=lib/stanford-postagger-3.3.1.jar -DgroupId=edu.stanford \
    -DartifactId=stanford-postagger -Dversion=3.3.1 -Dpackaging=jar
	@cd java-apps && mvn install:install-file -Dfile=lib/boilerpipe-1.2.0.jar -DgroupId=de.l3s.boilerpipe \
    -DartifactId=boilerpipe -Dversion=1.2.0 -Dpackaging=jar
	@cd java-apps && mvn install

# Run java services tests
java-test: java-bugs
	@cd java-apps && export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 && mvn test -DXmx1024M -DXX:MaxPermSize=128M \
	 -Dlog4j.configuration=../conf/log4j.local.xml -Dcheckstyle.skip=true

# Run java service code analysis tool (PMD+FindBugs)
java-bugs:
	@cd java-apps && export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 \
	&& mvn clean package findbugs:findbugs jxr:jxr pmd:pmd pmd:cpd pmd:check pmd:cpd-check \
	-Dpmd.printFailingErrors=true -Dmaven.test.skip=true -Dlog4j.configuration=../conf/log4j.local.xml

# Generate java documentation
java-doc:
	@cd java-apps && export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 \
	&& mvn jxr:jxr javadoc:javadoc  -Dlog4j.configuration=../conf/log4j.local.xml
	@cp -R java-apps/target/site/apidocs web-server/private
	@cp -R java-apps/target/site/xref web-server/private

# Build java tools
java-build: java-doc
	@cd java-apps && export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8 && mvn clean package \
		-Dlog4j.configuration=../conf/log4j.local.xml || (if [ "$(U_NAME)" = "Darwin" ]; \
		then say "Java build failed"; fi; exit 1)
	@chmod +x java-apps/dist/bin/mail_service
	@chmod +x java-apps/dist/bin/extractor_service
	@chmod +x java-apps/dist/bin/miner_service
	@if [ "$(U_NAME)" = "Darwin" ]; then say "Java build completed"; fi

# Run mail service (gather jobs sent by email)
java-run-mail:
	@./java-apps/dist/bin/mail_service "$(CURR_ROOT)" local

# Run extractor service (extract text and meta data from file, url, html...)
java-run-e:
	@./java-apps/dist/bin/extractor_service "$(CURR_ROOT)" local

# Run miner service (run text mining processors: annotation, classification...)
java-run-miner:
	@export JAVA_OPTS="-Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=256m" && \
		./java-apps/dist/bin/miner_service "$(CURR_ROOT)" local

# Rune Node services tests
js-test: .node-lint-test .node-closure-test

.node-lint-test:
	@gjslint -r --strict web-server/index.js || (say "Web server index is not linted" && exit 1)
	@gjslint -r --strict web-server/gulpfile.js || (say "Gulp file is not linted" && exit 1)
	@gjslint -r --strict \
		--jslint_error blank_lines_at_top_level \
		--jslint_error braces_around_type \
		--jslint_error optional_type_marker \
		--jsdoc web-server/src/* || (say "Web server source is not linted" && exit 1)

.node-closure-test:
	@find $(NODE_SOURCE) -name "*.js" -print0 | xargs -0 java -jar $(GCLOSURE) --js_output_file test.js --js || (say "Web server source compilation failed" && exit 1)
	@rm test.js

# Build modular and update it in chrome extension and web server
chunky:
	@cd chunk && gulp
	@cp chunk/dist/chunk.js chrome/vendors/

# Run all tests
test: java-test services-test node-test

# Test and build everything
build: test java-build

# Test and rebuild everything, init stuff, finally start services
start: init test build

.PHONY: \
	requires \
    java-test \
    java-bugs \
    java-build \
    services-test \
    test \
    build \
    start \
    help \
    init \
