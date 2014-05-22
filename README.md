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

## Other repositories requirements

In order to have the whole Minethat system running, you should clone in the main
directory (this repo) two other repositories: [web](https://github.com/Minethat/web)
and [corpora](https://github.com/Minethat/corpora).

## Content

```sh
    /conf          # Configuration
    /datasets      # Datasets used by java services
    /java-apps     # Java services
    /logs          # All logs for all processes and apps
    /utils         # Libraries
```

## Services

### Required services

- Mongo DB
- RabbitMQ

### Backend services

- `node web/src/server/aggregator.js`
- corpora/corpora -r datasets/corpora
- java-apps/dist/bin/mail_service
- java-apps/dist/bin/extractor_service
- java-apps/dist/bin/miner_service

### Front end service

- `node web/src/server/index.js`

## Web server

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

* NLP: Apache OpenNLP, Stanford POSTagger & NER
* Text extraction: Apache Tika, PDFBox
* HTML parsing: Jsoup

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






Sellthat
========

We believe that text-mining should be simple and accessible.

Here are a few examples of use:
* Rate website comments, propositions commerciales
* Annotate your content






# Spread the word

### Offline

* Visit cards
* Network of people

### Online

* Twitter
* LinkedIn
* Email footer
* Blog
* Reddit (r/linguistics/, r/MachineLearning/, r/LanguageTechnology/, r/compsci/, r/statistics/, r/opendata, r/startups)








### How it works

Just drag a text file (PDF, Word, Markdown...) and wait for the result.
You're developer? We have some APIs for you.

### The background

We use the best-in-class open source solutions in a modular way, letting you select what mining operation you want to run on texts. Once submitted, your text will be streamed accross dozens of processors that will analyse the text and annotate it.

# Technical introduction

Minethat utilities relies on different tools.

## Tech overview

### Java services

Text mining core services - core of Minethat offer - relies on a Java service. Main reason is the high number of open source and licensed Java APIs dedicated to various text mining tasks. All code lives in java-apps — IntellijIDEA project included.

### Web servers

All APIs are exposed through Node.js servers. Node is particularly efficient in serving stuff at any scale.


## Values

- Design to scale
- Code grammar nazi


## Benefits

- Save time and money
- Gain knowledge
- Improve your writing



## Features

- Text annotation
- Sentiment analysis
- Trend discovery
- Documents encryption
- SDKs: Java, Node.js, Python
- 3 APIs (Mail, REST, web) + Chrome Extension


# Everybody on the same line


### What we are (what do we want?)

* We are minethat, a compagny that aim to allow everyone to better use and understand textual content.


### Benefits (what is important to customers about what we do?)

* With our tool, customers benefit some really actionable metrics (quality, statictics, anotations).


### Customers (What are our most successful customer stories?)


### Key partners (What makes them successful using our products?)


### Competitors (How are we different from our competitors)

* Simplicity.
* Pricing.


# Services

### Premium support

By subscribing to Business plans, you automatically benefit the premium support access.

Premium support includes:
- Email tickets within 12 hours, 24/7

### Training service

Startup and business owners



# Pricing

----------------------------------------------------------
|                      | Basic   | Startup  | Business   |
|--------------------------------------------------------|
| Documents/month      | 10      | 1000     | Unlimited  |
| Web app submission   | x       | x        | x          |
| Email submission     |         | x        | x          |
| API submission       |         | x        | x          |
| Premium support      |         |          | x          |
| Initial training     |         |          | x          |
| Price                | Free    | $49/m    | $499/m     |
----------------------------------------------------------





# FAQ

### What languages do you support?

Right now we fully support english and french languages. We work hard in order to soon provide chinese, japanese, as well as german and spanish.

### What are the ways to submit a document?

Three ways:

* manually through our web application (app.minethat.com)
* programatically using our REST API
* or just send us your text by email, we'll send you back the result in minutes

### What is the technical process?

When you submit a document, a Job is automatically created and queued in our stream processing infrastructure. The document will go through different kind of processors, that will split the text into simple analyzable senquences of tokens. Once all processors are done, the job is

### Does it implies machine learning?

Definitely yes. We use corpuses based on content from Wikipedia, Google, New York Times, and more. You can also submit your own corpuses for your custom classification process.

### What is your Level Of Quality / Availability?

For Enterprise plans, we ensure that our infrastructure has an availabity rate of 99.90%.