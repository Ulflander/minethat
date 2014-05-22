Install minethat
================

## Setup DBPedia lookup

Go into your home folder. Follow instructions here: https://github.com/dbpedia/lookup#running-a-local-mirror-of-the-webservice.

Let's now create a supervisor wrapper. Go into `lookup` folder that you created earlier. Create this new file `start.sh`:

```
#!/bin/sh
/home/ubuntu/dbp-lookup/lookup/run Server ../dbpedia-lookup-index-3.8
```

Make it executable:

`chmod +x start.sh`

Create a supervisor conf (replace `/home/user/lookup-install/` by the path to your actual DbPedia lookup install):

```
[program:dbp-lookup]
directory=/home/user/lookup-install/lookup
command=/home/user/lookup-install/lookup/start.sh
user=ubuntu
```

Then reload and update supervisor conf:

```
supervisorctl reread
supervisorctl update
```

Now you can start DbPedia lookup using:

`supervisorctl start dbp-lookup`

## Get the repos

Go into your home folder. Clone main repo.

`git clone git@github.com:Minethat/minethat.git`

Go into main repo.

`cd minethat`

Clone secondary repositories into main repo:

`git clone git@github.com:Minethat/web.git`

`git clone git@github.com:Minethat/corpora.git`

Still in main repo, initialize local maven repo using:

`make init`

