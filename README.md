# ServerNews
ServerNews is a simple Minecraft server plugin that allows admins to send messages out to players.
## Features
- [MiniMessage](https://docs.advntr.dev/minimessage/) format support
- Timestamps on each message
- Messages are stored and sent to players when they log in, or immediately if they are already online
## Command Usage
- `/servernews help`: shows command usage
- `/servernews listMessages`: lists the messages in the system
- `/servernews clearMessages`: clears the messages list
- `/servernews addMessage <miniMessage>`: adds a message to the list and sends it out to the players
## Compiling
- To compile, clone the repo and run `./gradlew build`. The final jar will be in thr `build/libs` directory.
- To run a test server with the plugin installed, run `./gradlew runServer`. The server is 1.21.4 by default, but you can edit the `tasks.runServer.minecraftVersion` line in `build.gradle.kts` to change the version.
