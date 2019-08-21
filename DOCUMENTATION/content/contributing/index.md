---
title: 7. Contributing
type: index
weight: 7
---


## Have a Question



## Found an Issue

If you have an issue or you found a typo in the documentation, you can help us by
opening an [Issue](https://github.com/fcumselab/ProgEdu/issues).

**Steps to do before opening an Issue:**

1. Before you submit your issue search the archive, maybe your question was already answered couple hours ago (search in the closed Issues as well).

2. Decide if the Issue belongs to this project or to [Docker](https://github.com/docker) itself! or even the tool you are using such as Nginx or MongoDB...

If your issue appears to be a bug, and hasn't been reported, then open a new issue.

*This helps us maximize the effort we can spend fixing issues and adding new
features, by not reporting duplicate issues.*



## Want a Feature
You can request a new feature by submitting an [Issue](https://github.com/fcumselab/ProgEdu/issues) (it will be labeled as `Feature Suggestion`). If you would like to implement a new feature then consider submitting a Pull Request yourself.




## Update the Documentation (Site)

Laradock uses [Hugo](https://gohugo.io/) as website generator tool, with the [Material Docs theme](http://themes.gohugo.io/theme/material-docs/). You might need to check their docs quickly.

Go the `DOCUMENTATION/content` and search for the markdown file you want to edit

Note: Every folder represents a section in the sidebar "Menu". And every page and sidebar has a `weight` number to show it's position in the site.

To update the sidebar or add a new section to it, you can edit this `DOCUMENTATION/config.toml` toml file.

> The site will be auto-generated in the `docs/` folder by [Travis CI](https://travis-ci.org/fcumselab/ProgEdu/).



### Host the documentation locally

1. Install [Hugo](https://gohugo.io/) on your machine.
2. Edit the `DOCUMENTATION/content`.
3. Delete the `/docs` folder from the root.
4. After you finish the editing, go to `DOCUMENTATION/` and run the `hugo` command to generate the HTML docs (inside a new `/docs` folder).







## Submit Pull Request Instructions

### 1. Before Submitting a Pull Request (PR)

Always Test everything and make sure its working:

- Pull the latest updates (or fork of you don’t have permission)


### 2. Submitting a PR
Consider the following guidelines:

* Search [GitHub](https://github.com/fcumselab/ProgEdu/pulls) for an open or closed Pull Request that relates to your submission. You don't want to duplicate efforts.

* Make your changes in a new git branch:

     ```shell
     git checkout -b my-fix-branch master
     ```
* Commit your changes using a descriptive commit message.

* Push your branch to GitHub:

    ```shell
    git push origin my-fix-branch
    ```

* In GitHub, send a pull request to `ProgEdu:master`.
* If we suggest changes then:
  * Make the required updates.
  * Commit your changes to your branch (e.g. `my-fix-branch`).
  * Push the changes to your GitHub repository (this will update your Pull Request).

> If the PR gets too outdated we may ask you to rebase and force push to update the PR:

```shell
git rebase master -i
git push origin my-fix-branch -f
```

*WARNING. Squashing or reverting commits and forced push thereafter may remove GitHub comments on code that were previously made by you and others in your commits.*


### 3. After your PR is merged

After your pull request is merged, you can safely delete your branch and pull the changes from the main (upstream) repository:

* Delete the remote branch on GitHub either through the GitHub web UI or your local shell as follows:

    ```shell
    git push origin --delete my-fix-branch
    ```

* Check out the master branch:

    ```shell
    git checkout master -f
    ```

* Delete the local branch:

    ```shell
    git branch -D my-fix-branch
    ```

* Update your master with the latest upstream version:

    ```shell
    git pull --ff upstream master
    ```





<br>
#### Happy Coding :)
