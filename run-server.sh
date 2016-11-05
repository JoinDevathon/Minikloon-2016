#!/bin/bash

if [ ! -d "build" ]; then
    echo "Spigot is not downloaded, downloading and building now.."
    rm -rf build/
    mkdir build
    cd build

    curl -O https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
    java -jar BuildTools.jar --rev 1.10

    if [ ! -d "apache-maven-3.2.5" ]; then
        echo "Maven is not downloaded, downloading now.."
        curl -o apache-maven-3.2.5.zip http://mirror.metrocast.net/apache//maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.zip
        unzip apache-maven-3.2.5.zip
        rm apache-maven-3.2.5.zip
    fi

    cd ..

    chmod +x ./build/apache-maven-3.2.5/bin/mvn # for some reason this isn't executable by default..
fi

if [ ! -d "server/plugins" ]; then
    mkdir -p server/plugins
fi

if [ ! -f "server/spigot.jar" ]; then
    cp build/spigot-1.10.2.jar server/spigot.jar
fi

if [ ! -f "server/eula.txt" ]; then
    read -p "Do you accept the Mojang EULA? If not, then exit the program now. Otherwise, press Enter."
    echo "eula=true" > server/eula.txt
fi

_term() {
    echo "stop" > /tmp/srv-input
    exit
}

if [[ ! $(uname) == MING* ]]; then
    trap _term EXIT # only trap exit event if we're on unix
fi

while true; do
    if [ -f "build/apache-maven-3.2.5" ]; then
    	./build/apache-maven-3.2.5/bin/mvn clean install # use the maven that spigot build tools downloaded
    else
        mvn clean install
    fi
    cp target/DevathonProject-1.0-SNAPSHOT.jar server/plugins/DevathonProject-1.0-SNAPSHOT.jar
    cd server

    if [[ $(uname) == MING* ]]; then
        # we're running inside of git bash on windows, which doesn't support everything that unix systems do
        # so just run the jar and ask the user if they want to continue running after it's done
        java -jar spigot.jar

        read -n 1 -p "Do you want to recompile and restart the server? (y/n) " value
        if [ "$value" == "n" ]; then
            echo "Shutting down process.."
            exit
        fi
    else
        # set up out process
        rm -f /tmp/srv-input

        mkfifo /tmp/srv-input
        cat > /tmp/srv-input &
        tail -f /tmp/srv-input | java -jar spigot.jar &

        running=true
        while $running; do
            read input
            if [ "$input" == "stop" ]; then
                running=false
                echo "stop" > /tmp/srv-input
            elif [ "$input" == "exit" ]; then
                running=false
                echo "stop" > /tmp/srv-input
                sleep 2
                exit
            else
                echo "$input" > /tmp/srv-input
            fi

            sleep 1
        done
    fi

    cd ..

    echo "Rebuilding project.."
    sleep 1
done
