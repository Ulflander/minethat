Install minethat
================

This guide will go through a whole Minethat install and run setup.

First you have to check if dependencies are installed, if not please install them.

Second Minethat requires DbPedia lookup. We'll install it.

At last we'll install Minethat app and create wrappers to have all processes running.

## Dependencies

Make sure you have these dependencies ibnstalled on your system:

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
- pm2

## DBPedia lookup install

Go into your home folder.

Create a folder like `lookup-install`.

Follow instructions here: https://github.com/dbpedia/lookup#running-a-local-mirror-of-the-webservice.

Let's now create a supervisor wrapper. Go into `lookup` folder that you created earlier. Create this new file `start.sh` (replace `/home/user/lookup-install/` by the path to your actual DbPedia lookup install):

```
#!/bin/sh
/home/user/lookup-install/lookup/run Server ../dbpedia-lookup-index-3.8
```

Make it executable:

`chmod +x start.sh`

Create a supervisor conf (replace `/home/user/lookup-install/` by the path to your actual DbPedia lookup install, and `user=user` by your actual system user name, for example `user=ubuntu`):

```
[program:dbp-lookup]
directory=/home/user/lookup-install/lookup
command=/home/user/lookup-install/lookup/start.sh
user=user
```

Then reload and update supervisor conf:

```
supervisorctl reread
supervisorctl update
```

Now you can start DbPedia lookup using:

`supervisorctl start dbp-lookup`

# Minethat install

### Before starting

##### ENV

A few things have to be done before starting install and run Minethat processes.

First, configure environment so processes know if we're on development or production:

`sudo nano /etc/environment`

add an `ENV` variable set to `local`, `dev`, `test`, or `prod`:

```
ENV=dev
```


### Get the repos

Go into your home folder. Clone main repo.

`git clone git@github.com:Minethat/minethat.git`

Go into main repo.

`cd minethat`

Clone secondary repositories into main repo:

`git clone git@github.com:Minethat/web.git`

`git clone git@github.com:Minethat/corpora.git`

Still in main repo, initialize local maven repo using:

`make init`

