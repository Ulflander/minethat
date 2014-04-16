Minethat
========

Minethat is a new kind of [ETL](http://fr.wikipedia.org/wiki/Extract_Transform_Load) dedicated to text mining.

It contains a web server (in Node.js) and some background services (update of data, mining services... in Java).

## Basics

### How to use

To get started, run `make` in your terminal:

```
make
```

## Requirements

- MongoDB
- RabbitMQ
- Java 1.7 + Maven
- Node.js latest + npm
- Nginx
- Git + gitflow
- ImageMagick
- Cube.js
- monit
- supervisor


## Content

```sh
~/app
~/app/conf          # Configuration
~/app/datasets      # Datasets used by java services
~/app/java-apps     # Java services
~/app/lib           # Some libs used by makefile
~/app/logs          # All logs for all processes and apps
~/app/scripts       # Handy scripts
~/app/web-server    # Web server
```

## Services

### Backend services

- java-apps/dist/bin/mail_service
- java-apps/dist/bin/extractor_service
- java-apps/dist/bin/miner_service

### Front end service

- web-server/index.js

## Web server

Based on flatironjs.

Uses gulp.js for build:

```
$ cd web-server
$ gulp
```

Use `gulp watch` while working to refresh files. 

This module serves:

* Homepage (/)
* Web application (/app)
* Blog (/blog)
* Developer center (/developers)
* Private documentations (/private)

### Good to know

- Root folder "static" is automatically generated from "src/static"

- How to add new user/pass in users.htpasswd:
    * `npm install -g htpasswd`
    * `htpasswd -bc users.htpasswd user pass`


## Java services

### Logging/testing/code analysis

* Logging: log4j 2
* Testing/code quality:
    * jUnit
    * findbugs, PMD, checkstyle, cobertura
* In IDE code analysis: IntellijIDEA code analyzer

### Text mining

* NLP: Apache OpenNLP, Stanford POSTagger
* Text extraction: Apache Tika

### Datasources

* MaxMind GeoIP 2

### Linked data

* OpenRDF

### How it works

MailInputService and web-server generate some Jobs, save them in MongoDB, get the ID, 
and submit ID to queue input service that will run the job and process each document in it.


```
MailInputService >
                    > ExtractorService > MinerService
Web-server       > 
```


## Todo

### At first deployment

These tasks are to be done before first deployment:

- Configure log4j file appenders so it targets log files
- Configure log4j mongodb appender
- Configure tracer file appenders + mongodb appenders
- Configure MongoDB replication (x2)


### Before launching

- Bug UI
- Builds UI
- Customer history UI
- SSL
- OAuth



```
curl -H "Accept: application/json"  \
  "http://spotlight.dbpedia.org/rest/annotate/?text=French%20media%20goes%20crazy%20over%20rumor%20that%20President%20Obama%20and%20Beyonce%20are%20having%20an%20affair%0AFrench%20photographer%20who%20discovered%20that%20President%20Francois%20Hollande%20was%20having%20an%20affair%20claims%20that%20President%20Obama%20is%20cheating%20as%20well%0ACited%20alleged%20%27distance%27%20that%20has%20been%20%27apparent%27%20between%20Barack%20and%20Michelle%20Obama%20recently%20%0AThe%20Obamas%20are%20open%20fans%20of%20Beyonce%20and%20her%20husband%20Jay%20Z%0A%0A%0ARead%20more%3A%20http%3A%2F%2Fwww.dailymail.co.uk%2Fnews%2Farticle-2556239%2FFrench-media-goes-crazy-rumor-President-Obama-Beyonce-having-affair.html%23ixzz2tIQMf5mF%20%0AFollow%20us%3A%20%40MailOnline%20on%20Twitter%20%7C%20DailyMail%20on%20Facebook&spotter=LingPipeSpotter"

curl -H "Accept: application/json"  \
"http://spotlight.dbpedia.org/rest/annotate/?text=President%20of%20the%20United%20States"
```