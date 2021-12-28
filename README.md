![logo](https://i.imgur.com/MLKboYN.png)
# vCore
### Because Essentials and EssentialsX give to much!
vCore was started to get a more barebone fashion to essential plugins.
As a history as a server owner, private server enthusiast and programmer I've always thought that;
The Essentials line gave to much "shite" than we ever needed and just cluttered the server full with
unneeded and clunky commands.
User directories from the plugins have a big window to corrupt due to the way they flat files are kept so we've started to create vCore!

### Plugins created with vCore
- [vTeleportation](https://github.com/MichaJDev/vTeleportation/)


### How code with vCore
Firstly you need to implement it into your jars
(Yes this plugin has not been setup with Maven or anything similar this is a jar library)

__plugin.yml__
```
depends: vCore
```
__Main.java__
Add this to your onEnable() if you are using any of the builders created in vCore
for instance if you use the UserBuilder it'll be written down like:
```
private IUserBuilder users;
private vCore vMain = vCore.Main.getInstance();
public void onEnable(){
    if(getServer().getPluginManager().getPlugin("vCore")!=null){
      users = new UserBuilder(vMain);
    }
}
```

this makes you able to use anything that #UserBuilder implements.

###Investors&Supporters
Are you interested to support his project in anyway, pls feel free and email me
[My Email](michajanssen93@gmail.com)
