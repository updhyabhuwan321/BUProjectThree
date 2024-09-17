Cell Tower Coverage Simulator
This project simulates cellphone tower coverage for a series of waypoints and journeys. It reads data from files containing cellphone tower definitions, waypoint definitions, and a set of waypoint names for potential journeys, and generates a detailed report on the coverage provided by the towers.

Features
Calculates distances between waypoints in each journey.
Identifies the closest cell tower to each waypoint and mid-point along the journey.
Checks whether the waypoints and mid-points have cell coverage from nearby towers.
Generates a detailed report of the journey and cell tower coverage, including distances and coverage proximity.
Files
CellTower Data File: Contains definitions of cell towers (name, x-coordinate, y-coordinate, range).
Waypoint Data File: Contains definitions of waypoints (name, x-coordinate, y-coordinate).
Journeys File: Contains sets of waypoints representing different journeys.

Input the required file names when prompted:

Cell tower file
Waypoint file
Journeys file
The program will generate a Report.txt file that includes detailed information about waypoint distances, midpoints, and cell tower coverage.

Output
The program will generate a report named Report.txt that provides the following:

Journey details with waypoints.
Mid-point calculations between waypoints.
Coverage information from the nearest cell towers.
Contributing
Pull requests are welcome. For major changes, please open an issue to discuss what you would like to change.
