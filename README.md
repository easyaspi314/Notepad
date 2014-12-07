Notepad
==
<h3>Simple, easy-to-use, themed notepad forked from [an old project by BanderLabs](http://code.google.com/p/banderlabs).</h3>

<h4>How to build:</h4>
 1. Clone the following projects:
  * This.
  * [AppCompatFragmentListActivity](https://github.com/easyaspi314/AppCompatFragmentListActivity)
  * [LicensesDialog](https://github.com/PSDev/LicensesDialog)
  * [android-support-v4-preferencefragment](https://github.com/kolavar/android-support-v4-preferencefragment)
 2. Import these projects into ADT.
 3. Add the libraries you just cloned to the project.
 4. Compile, and install on a ICS+ device.

I would also like to use Fragments (yuck) to prepare for a 2-column view, but 
it is really hard trying to migrate to it. Why did they have to make it soo 
difficult?!

---- 
There are two NoteList screens. One is for the normal theme [NoteListLight.java](src/bander/notepad/NoteListLight.java) and one is for the AppCompat theme [NoteListAppCompat.java](src/bander/notepad/NoteListAppCompat.java). It just uses different icons. That is 100% planned.

The Startup activity is the core of this app. It is an app that sets the theme for each Activity, launches the right NoteList, and launches the password screen

My themes are in styles.xml, styles_appcompat.xml, and styles_kitkat.xml.. The light theme starts on Holo Light and adds selector and button colors. The Dark theme is a bit more complex, 
using the same elements as above, but the Action Bar is borrowed from Theme.Holo.Light.DarkActionBar and the background is regular Holo.

<h4>Notice </h4>
I give all of my credit to BanderLabs for making the Notepad tutorial app 1000 times better! It is 80% of the same code.

<h4>The major things I did:</h4>
- Switch between Material (AppCompat), KitKat Holo, ICS/JB Holo, "DeviceDefault" to satisfy TouchWiz fans, and classic Gingerbread (but why? This is Holo themed...). Choose a light, light/darkactionbar, or dark version. Only has one theme for Material theme.
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

<h4>To do </h4>
- HDPI, XHDPI and XXHDPI. I only included MDPI drawables because my phone's screen is MDPI.
- Tablet-friendly. "Honeypad" demo seems good, but hard to implement, because of Fragments.
- Fragments, but from the look of it, it will require a whole rewrite.
- A ton of code updates. This is 2011 code, and it has a *LOT* of deprecated code. Especially some cursor management. I suck at that.
- Some dialog messages should have positive/negative buttons, not using layout buttons.
<h4>Requested in comments on BanderLabs' Wiki</h4>
- Smarter import/export. 
- dpl0...@gmail.com - Encryption of backups?... How? Password-protected ZIP?
- dpl0...@gmail.com - auto backup
- cagilber...@gmail.com - Timestamps
- ~polkadot...@gmail.com - Lock/hide Password!~
- b...@xpressionmedia.com - Import CSV
- Desktop sync.... with what? Wouldn't that make the app bloated?!
