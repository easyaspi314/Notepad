#Notepad

###Simple, easy-to-use, themeable notepad forked from [an old project by BanderLabs](https://code.google.com/p/banderlabs).

Release APK coming soon.
**This project isn't dead, I have it at low priority (I am working on other projects) and am trying to work on LP/MM support.**

####How to build:
 1. Import these projects into Android Studio.
 2. Compile, and install on a ICS+ device.

---- 
There are two NoteList screens. One is for the normal theme [NoteList.java](app/src/main/java/bander/notepad/NoteList.java) and one is for the AppCompat theme [NoteListAppCompat.java](app/src/main/java/bander/notepad/NoteListAppCompat.java) which extends ActionBarActivity. There are a lot of duplicate classes because all AppCompat activities must extend from ActionBarActiviy and ActionBarActivity will crash if not using AppCompat. This means that there needs to be two activities for each screen, and two layouts, one with the Toolbar and one without. This is also seen in, for example, [NoteEditAppCompat.java](app/src/main/java/bander/notepad/NoteEditAppCompat.java) and [edit_appcompat.xml](app/src/main/res/layout/edit_appcompat.xml).

The [Notepad](app/src/main/java/bander/notepad/Notepad.java) activity is the core of this app. It supplies:
- Launches the right NoteList on startup, as noted above.
- Sets the theme, that is why you see `Notepad.setThemeFromPreferences(this);`. This is based on [OpenSudoku.](https://github.com/romario333/opensudoku)
- Launches the password protection. Still in beta, it needs work.
- Changes the Toolbar color

My themes are in [styles.xml](app/src/main/res/values/styles.xml), [styles_appcompat.xml](app/src/main/res/values/styles_appcompat.xml), and [styles_kitkat.xml](app/src/main/res/values/styles_kitkat.xml). The light theme starts on Holo Light and adds selector and button colors. The Dark theme is a bit more complex, using the same elements as above, but the Action Bar is borrowed from Theme.Holo.Light.DarkActionBar and the background is regular Holo.

####Notice
I give a lot of credit to BanderLabs for making the Notepad tutorial app 1000 times better! It is the base of the code that made this great app. The code is a lot different because I have been working on it for a while and only open-sourced it recently.

####The major things I did:
- Switch between themes:
  * Material (AppCompat)
  * KitKat Holo (desaturated)
  * Default Holo theme
  * Device Default to satisfy TouchWiz fans
  * Classic Gingerbread (but why? This is Holo themed...). 
  * Choose a light, mixed, or dark version. When using the Material theme, you can choose the color of the Toolbar.
- Licenses Dialog
- Option for Monospace font
- Option to toggle AutoCorrect
- Converted small activities to Dialogs. NOTE: I need to have actual dialog buttons, not actual buttons.
- Organized PreferenceScreen
- Action Bar, and Toolbar
- Renamed weird strings.
- New icon, but not mine. Looking to change this to one I designed.
- Organized/optimized some activities, like reordering the buttons, to comply with ICS.
- PreferenceFragments!!!
- Password protect. Very basic.

####To do:
- HDPI, XHDPI and XXHDPI drawables. I only included MDPI drawables because my phone's screen is MDPI.
- Tablet-friendly. "Honeypad" demo seems good, but hard to implement, because of Fragments.
- Fragments, but from the look of it, it will require a whole rewrite.
- SearchView for Holo/Material themes. I need help on this. 
- A ton of code updates. This is 2011 code, and it has a *LOT* of deprecated code.
 * I would like help with Cursor management. I get a lot of `Cursor finalized without prior close()` logs, because I removed all the `cursor.close()` for crash problems.
- Some dialog messages should have positive/negative buttons, not using layout buttons.
- Translations. Anyone?
- Clean up code. I have a lot of commented-out code.
- Play Store/F-Droid?

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
