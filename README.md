#Notepad

###Simple, easy-to-use, themed notepad forked from [an old project by BanderLabs](https://code.google.com/p/banderlabs) .

####How to build:
 1. Clone the following projects:
  * This.
  * [AppCompatFragmentListActivity](https://github.com/easyaspi314/AppCompatFragmentListActivity)
  * [LicensesDialog](https://github.com/PSDev/LicensesDialog)
  * [android-support-v4-preferencefragment](https://github.com/kolavar/android-support-v4-preferencefragment)
 2. Import these projects into ADT.
 3. Add the libraries you just cloned to the project.
 4. Compile, and install on a ICS+ device.

---- 
There are two NoteList screens. One is for the normal theme [NoteListLight.java](src/bander/notepad/NoteListLight.java) and one is for the AppCompat theme [NoteListAppCompat.java](src/bander/notepad/NoteListAppCompat.java). This is because all AppCompat activities must use ActionBarActiviy and ActionBarActivity will crash if not using AppCompat. This is also the same way, with two clones of the activity, and also two layouts, one with the Toolbar and one without.

The [Startup](src/bander/notepad/Startup.java) activity is the core of this app. It supplies:
- Launches the right NoteList on startup, as noted above.
- Sets the theme, that is why you see `Startup.setThemeFromPreferences(this, "class");`. This is based on [OpenSudoku.](https://github.com/romario333/opensudoku)
- Launches the password protection. Still in beta, it needs work.


My themes are in [styles.xml](res/values/styles.xml), [styles_appcompat.xml](res/values/styles_appcompat.xml), and [styles_kitkat.xml](res/values/styles_kitkat.xml). The light theme starts on Holo Light and adds selector and button colors. The Dark theme is a bit more complex, 
using the same elements as above, but the Action Bar is borrowed from Theme.Holo.Light.DarkActionBar and the background is regular Holo.

####Notice
I give a lot of credit to BanderLabs for making the Notepad tutorial app 1000 times better! It is the base of the code that made this great app. The code is a lot different because I have been working on it for a while and only open-sourced it recently.

####The major things I did:
- Switch between themes:
  * Material (AppCompat) 
  * KitKat Holo (desaturated)
  * Default Holo theme
  * Device Default to satisfy TouchWiz fans
  * Classic Gingerbread (but why? This is Holo themed...). 
  * Choose a light, mixed, or dark version. Only has one theme for Material theme, soon to change.
- LicensesDialog
- Option for Monospace font
- Option to toggle AutoCorrect
- Converted small activities to Dialogs. NOTE: I need to have actual dialog buttons, not actual buttons.
- Organized PreferenceScreen
- Action Bar, and ActionBarCompat
- Renamed weird strings.
- New icon, but not mine. Looking to change this to one I designed.
- Organized/optimized some activities, like reordering the buttons, to comply with ICS.
- PreferenceFragments!!!
- Password protect. Very basic.

####To do:
- HDPI, XHDPI and XXHDPI drawables. I only included MDPI drawables because my phone's screen is MDPI.
- Tablet-friendly. "Honeypad" demo seems good, but hard to implement, because of Fragments.
- Fragments, but from the look of it, it will require a whole rewrite.
- A ton of code updates. This is 2011 code, and it has a *LOT* of deprecated code. Especially some cursor management. I suck at that. Help wanted.
- Some dialog messages should have positive/negative buttons, not using layout buttons.
- Translations. Anyone?
- Clean up code. I have a lot of commented-out code.
- Play Store/F-Droid

####Requested in comments on BanderLabs' Wiki
- Smarter import/export. 
 * I don't know. 
- dpl0...@gmail.com - Encryption of backups?
 * How? Password-protected ZIP?
- dpl0...@gmail.com - auto backup 
 * This would be easy to do I think...
- cagilber...@gmail.com - Timestamps
 * I guess, there is already a sort function.
- b...@xpressionmedia.com - Import CSV
 * This uses TXT files. Put any notes you want to import into /sdcard/Notepad and Import.
- Desktop sync
 * With what? Syncthing? Wouldn't that make the app bloated?!
