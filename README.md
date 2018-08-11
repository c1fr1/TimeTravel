#NWAPW - Time Travel Puzzle Game


USABILITY:

-Use the robot to complete puzzles by traveling between timezones

-Levels increase in difficulty

-Levels are completed by activating a gateway block in the level


MENUS:

-Loads in each level from a text file of characters using a tile key

-Options allow the user to change video settings as well as rebind keys

-Level Selector allows the player to replay completed levels

-Pause Menu can be accessed by ESC


MECHANICS:

-Robot is controlled by the player

-To travel to a different timezone, the player must move near a time travel device: an interface will allow the user to select the time period they wish to travel to

-An inventory at the center bottom constantly displays keys currently held

-A miniature icon directly above the inventory will indicate the current timezone

-Keys can be collected and then used to open Locked doors: one key will be removed from the inventory

-Locked doors act the same as walls until they are unlocked: they then become open

-If a door is unlocked all future tiles in that location will also be open, regardless of the tile there previously

-If a key is collected all future versions of that key will no longer exist: the same is not true for the past

-One way doors can be moved through in all directions except from the direction the arrow is pointing: entities must be outside of the door for it to function

-Boxes are entities that can be pushed by players or other boxes; They can only move through any barrier a player is able to

-Buttons are interactive objects that can be pressed by a box or a player; It will open its color coded door

-Button Doors are barriers that are passable while their corresponding button is pressed

-If a box is placed in an area that would result in it being in a barrier in another timezone, it will not exist in that timezone until it is removed from the area

-Some time travel devices will have not be able to transport the player to all time zones

MAKING YOUR OWN LEVEL - use the following key to make levels
#	Wall
g	Gate (end of lvl)
k	Key
c	Cryochamber
(space)	background tile
s	Starting place
t	tto
_ Empty Void - Uninhabitable
l	Locked gate
^	oneway UP
>	oneway RIGHT
v	oneway DOWN
<	oneway LEFT
b	Box
w	button1
x	button 2
y	button 3
z	button 4
W	Door 1
X	Door 2
Y	Door 3
Z	Door 4

IN ADDITION YOU MUST HAVE A cfg FILE - this tells the game where each time travel device is allowed to go
Format:

