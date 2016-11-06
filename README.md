# Devathon Project
This is a project for Devathon of November 2016. The theme was "Machines".

The idea of this plugin is based on Animusic's videos : A music machine.
Here's a video of the plugin in action : https://www.youtube.com/watch?v=bfEGqEnOUYs

## Commands

**/supernote** : Gives you a super note gun.
Super notes are the core feature of the plugin. They're blocks flying around bouncing on stuff.
When they hit certain blocks, they play a sound.
Super notes from the gun will be note blocks.

**/stringmaker** : Gives you the string to tie blocks together.
This tool lets you leash blocks between eachothers. Super Notes will bounce off the leash and produce a sound with a pitch based on the leash length.

**/stringcutter** : Gives you shears which can cut strings.

**/spawnerwand** : Gives you a super note spawner wand.
The wand tool lets you pick blocks to become spawners. Spawners will automatically spawn super notes.
You can use interact and shift-interact with the wand in hand on a spawner to change its spawn frequency.
Super notes will spawn with the type of their spawner.

**/eartrumpet** : Gives you an ear trumpet.
The ear trumpet lets you hear every sound produced by super notes from anywhere in the world.

**/sample** : Gives you one of each configured blocks.

**/clearmusic** : Removes all super notes and strings.

## Permissions

- musicmachine.supernote
- musicmachine.stringmaker
- musicmachine.stringcutter
- musicmachine.spawnerwand
- musicmachine.eartrumpet
- musicmachine.clearmusic
- musicmachine.sample

## Configuration

```
world: "world"

sounds:
  defaultString: "block.note.harp"
  blocks:
    "24:0": "block.note.snare"
    "80:0": "block.note.basedrum"
    "41:0": "block.note.hat"
    "49:0": "block.note.bass"
  muted: ["35:15"]
```

The "defaultString" is the default sound played when a block hits a string.
You can view available sounds in vanilla Minecraft at http://pokechu22.github.io/Burger/1.10.html#sounds

The "blocks" map are blocks configured to play a specific sound when either they are hit by a super note **or** a super note of that type hits a string. "24:0" represents "id:data", for example "24:0" is default sandstone. There can be as many blocks in that list as you want.

The "muted" list are blocks which will stop the melting sound of super notes. By default that is black wool.

## Notes

The state of the music entities (super notes, spawners, strings...) are saved on graceful server exit in a *musicmachine.bin* file located in the world's folder.

This plugin was only tested on a local machine. It's possible that running it on a remote server will be impossible, cause massive lag or make kittens cry.
