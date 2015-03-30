# FlingableCards

###### Flingable cards for android. An implementation of Tinder-like cards using android CardView as a base. 

The cards respond to the fling gesture (hence the name) and differentiate 2 different cases : fling towards the top right quarter or top left quarter.
On each different cases a different callback is fired allowing for distinct behaviors when flinging to the right/top right or left/top left.

The repo contains the library and a sample app for you to test out.

#### Installing/Importing the library

1. Download or clone the repo.
2. Copy the "flingablecards" folder. It contains every file you need to add it to your own project.
3. Add a new module to your android studio application by clicking "Project structure" > green + > "import existing project"
4. The library should appear in you project file tree. You are good to go !

#### Usage

The flingable cards are based on android CardViews so they have the same customization possibilities. However, some attributes have been added to the CardContainer classe to be able to customize all the cards contained.

+ corner_radius : applies radius in pixels to the corner of all the cards.
+ displayed_cards : number of cards displayed at the same time. Depending on the device, this could make the application slow.  5 seems to be a good compromise since it gives enough depth not to see the background while not loading too much cards.
+ messy : boolean parameter allwing the cards to be tilted from a slight angle giving them a messy look. The default angle is   between -5° and +5°. Setting this to false tidies up the stack.
+ angular_amplitude : This parameter will only work if the "messy" parameter is set to true. It allows for modifying the   angular amplitude of the tilt between -angular_amplitude° and +angular_amplitude°

