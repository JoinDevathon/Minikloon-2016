# Devathon Project
This is the base layout for your Devathon Project. It includes several scripts to make running incredibly easy on Windows, Mac, and Linux.

## Help

Help will be available for 25 hours during the contest at the following sources:

Twitter: https://twitter.com/JoinDevathon
Discord: https://discordapp.com/invite/qNxMS5B

## Theme

The theme for the 2016 Devathon Contest is: **Machines!**
Make a machine, make an interaction with a machine, or do something completely creative! As long as it has something to do with machines, you're good to go.

## Reminders

Finish by November 6th at 8AM Central Time. You can find this time in your local timezone here: https://encrypted.google.com/search?hl=en&q=8%20am%20central%20time

## Rules


1.  Teaming is not allowed.
2.  No usage of public code & libraries.
3.  Streaming is allowed.
4.  Purposely copying another person’s idea is not allowed.
5.  You are not allowed to use code written before the contest has started.
6.  Code will be pushed regularly.
7.  Binaries should not be pushed.
8.  Accepting pull requests is not allowed.
9.  Code must be able to compile, we will not fix compile errors.
10. You must use Java 8.
11. Your plugin must fit the theme or it will be disqualified.
12. Maven is required.


## Get Started

**If you already know how to use Maven, then more than likely the following steps are irrelevant for you. Just do your usual thing.**

---

Don't worry, we made a video! Check it out at https://www.youtube.com/watch?v=u5HXS0l-VwQ

---

First things first, you need to have the Java JDK8 installed. You can find the appropriate version here: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Then you need to have git installed. You can find the appropriate version for your OS here: https://git-scm.com/

There are no other required dependencies, however Maven is optional if you want to set up your own development environment outside of what we do for you.

## Running

If you're on Windows, you'll want to run all of these commands inside Git Bash, which is a program installed when you installed Git. You can paste by right clicking inside of the window.

If you don't have this Git repository cloned yet, click on clone or download. If you have an SSH key on your account, use the SSH link. Otherwise use the HTTPS link if you want to use your GitHub username and password.

Then run

```bash
git clone <link>
```

To run your server, do:

```bash
./run-server.sh
```

On first run this will download and compile the Spigot version that you're using for the contest. Because you're using this exact version of Spigot for the entire contest, you can safely use CraftBukkit and Minecraft Server code.

This wrapper around the Spigot server has a few extra features that are not included inside of regular Spigot. If you type `stop` to stop the Minecraft Server, it'll automatically recompile your code and restart the server. To fully stop the server, type `exit` to safely stop the server and exit the recompilation loop. If you're on Windows, you won't have a `exit` command and will instead be asked every loop if you want to recompile.
