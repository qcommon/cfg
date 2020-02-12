# qcommon-cfg

_qcommon-cfg_ is a configuration library for Minecraft. Unlike most other
configuration libraries however, the configuration files it writes are more
like scripts and not serialized data structures. This allows it to be more
flexible from the user's side, since all the values used in the configuration
files created by _cfg_ can also be set from other scripts.

## For Developers

### Usage in a Development Environment

Merge this snippet with your build.gradle:

```
repositories {
    maven { url = 'https://maven.dblsaiko.net/' }
}

dependencies {
    modRuntime(group: 'net.dblsaiko.qcommon.cfg', name: 'cfg', version: '2.0.2-20') {
        exclude group: 'net.fabricmc.fabric-api'
    }
    
    modImplementation(group: 'net.dblsaiko.qcommon.cfg', name: 'cfg-core', version: '2.0.2-20') {
        exclude group: 'net.fabricmc.fabric-api'
    }
}
```

This will make your mod depend on _cfg-core_ only, which contains the main API
to interact with _qcommon-cfg_, but will still run with all of _qcommon-cfg_ in
the development environment, so that you have access to e.g. the in-game
console, which doesn't come with _cfg-core_.

It's also recommended to pack _qcommon-cfg_ into your mod so that users don't
have to download it seperately if they don't want the extra features the full
version contains (which is the only version available for download on
CurseForge.) If you wish to do so, also merge this snippet:

```
dependencies {
    include group: 'net.dblsaiko.qcommon.cfg', name: 'cfg', version: '2.0.2-20'
}
```

### Configuration

#### Creating a Configuration File

The basic use-case for _qcommon-cfg_ is to provide configuration files for
mods. The API for this is fairly straightforward, here's the most basic
example:

```
ConfigApi.Mutable api = ConfigApi.getInstanceMut();

api.addConVar("mymod_power_per_tick", IntConVar.owned(200));
```

This will register a new cvar (Console Variable) named 'mymod_power_per_tick'
storing an integer, which is set to 200 by default. Due to the (deliberate)
lack of forced namespacing, it is highly recommended to add a prefix ('mymod'
in this case) to the name of your cvars to prevent name clashes. This prefix
should ideally identify your mod, but not be overly long to prevent having cvar
names that are too long. For example, for RetroComputers I'd use the prefix
'rc', or some other similar short name if that were already taken.

As it is right now, those two lines will add the cvar, but it will not be saved
nor synchronized across the network, nor can you access the value.

#### Getting/Setting Values Stored in Cvars

For this, you have two options. For the cvar types provided by _cfg_, there's
two methods of creating a cvar, differing in where the actual value the cvar
holds is stored: `owned` and `wrap`.

`owned` creates a cvar that owns its value, or stores the value inside of
itself. To access the value, you can use the respective `get` and `set`
methods on the ConVar instance. `api.addConVar` conveniently returns that
instance, so you can assign it to a field/variable:

```
IntConVar powerPerTick = api.addConVar("mymod_power_per_tick", IntConVar.owned(200));

int value = powerPerTick.get();
powerPerTick.set(1000);
```

`wrap` creates a cvar that delegates to another field using `Ref<T>` (or
`IntRef`/`FloatRef`.) These can be created with `Ref.from`, and either take a
`Field` and target `Object` (or `null` in the case of a static field), or a
getter and setter lambda. The latter is recommended because it doesn't use
reflection/field lookup by string and is generally shorter to write (no
exception handling needed for getting the `Field` instance).

```
api.addConVar("mymod_power_per_tick", IntConVar.wrap(IntRef.from(() -> powerPerTick, value -> powerPerTick = value)));

int value = powerPerTick;
powerPerTick = 1000;
```

This has the benefit that you can directly access the value, with all drawbacks
that come with this, for example there will not be any checks that the value is
valid, for example in the case of number cvars that have a value range.

#### Saving Cvars to a Configuration File

To get _cfg_ to store cvars, you can pass `CvarOptions` when registering the
cvar. You can reuse these for multiple registrations, they're immutable.

```
api.addConVar("mymod_power_per_tick", IntConVar.owned(200), CvarOptions.create().save("mymod"));
```

This will cause 'mymod_power_per_tick' to get saved in the file
'config/mymod.cfg' and cfg to execute that file on startup to restore the value
of that cvar.

Cvars with the save option will be automatically saved to disk when the game
shuts down. You can force them to be saved immediately by executing the `save`
command if you have _cfg-ui_ installed.

#### Server -> Client Synchronization

Making _cfg_ synchronize cvars with connected clients is very similar to making
them save to a configuration file:

```
api.addConVar("mymod_power_per_tick", IntConVar.owned(200), CvarOptions.create().sync());
```

This will cause the server to send the client the value for this cvar when it
connects, and prevent the user from modifying that cvar on the client for as
long as it is connected. Any changes to the cvar on the server side will be
automatically propagated to the client.

### Commands

_TODO_

```
api.addCommand("test", (args, src, output, cf) -> output.print("Hello World!"));
```

## For Players

### Scripting

_TODO_

#### Important Commands for Scripting

|Command|Function|Example Usage|
|-------|--------|-----|
|`alias`|Create an alias. When the alias is executed, it will expand to its content.|`alias save-quit "save; quit"`|
|`unalias`|Delete an alias.|`unalias save-quit`|
|`wait`|Wait one tick. On the client, this is one frame, on the server, this is one world tick.|`wait`|
|`exec`|Execute a script from the config directory.|`exec mymod`|

Aliases don't get automatically saved. To make them persistent, put them in
'config/autoexec.cfg' in the game directory. This script gets automatically run
on game/server startup.

### Keybindings [WIP]

_(This is not implemented yet in the current version of qcommon-cfg.)_

_cfg-keys_, if installed, overhauls how keybindings work. Instead of being a n:1
mapping of keybinding->key, it is a n:1 mapping of key combination->script.
Effectively, this means multiple things:

 - You can assign multiple keys to do the same thing. Not sure why you'd want
   to, but you can.
 - You can assign arbitrary key combinations instead of a single key per binding
   only, for example shift+n, alt+shift+tab or even unconventional bindings such
   as a+d. (Get the snapshot boat controls back that they removed at some
   point!)
 - You can still make a single key do multiple actions, but this time in a much
   more controlled manner. Since the target of a key binding is a script, you
   can bind multiple commands to it no problem: `bind ctrl+1 "slot1; drop_slot"`

Keybindings executing a single command starting with a '+' will execute the
same command when the key is released, but replacing the '+' with a '-'. This
allows for binds that have an effect as long as they're held down, such as the
movement keys. (If you need this for more than one command, you need to define
aliases such as `alias +sprint "+walk; +run_on"; alias -sprint "-walk;
-run_on"`)

For compatibility with non-_cfg_ keybindings, the `key_wrapper` and
`+key_wrapper` commands are available. They the key name as the argument, for
example 'attack' for the vanilla attack bind. _cfg-keys_ comes with a wrapper
for all the vanilla keys, in this case `+attack`/`-attack`, but in this case
you could also gain the same effect with `bind mouse1 "+key_wrapper attack"`.
