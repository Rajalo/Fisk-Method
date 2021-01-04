# Fisk Method Guarding Number Estimator

In this calculator, I adapted the algorithm described in Fisk's proof of the upperbound for the guarding number of a simple polygon being the floor of the number of vertices divided by three. It takes a polygon, triangulates and then does a 3-coloring of the vertices. After counting how many vertices there are of each color, it produces an upper limit for the guarding number of that polygon.

The program has 3 phases, the DRAWING phase, the TRIANGULATION phase, and the FINAL/DISPLAY phase.

In the DRAWING phase, the user draws in a polygon using the mouse, left-clicking to add vertices between the first and last added and right-clicking to remove the closest vertex to the mouse.

In the TRIANGULATION phase, the program shows the triangulation that it found using the Ear-Clipping Method.

In the FINAL/DISPLAY stage, the program shows the 3-coloring and displays both the vertex counts for each color and the upper bound found for the polygon.