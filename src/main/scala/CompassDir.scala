package zombies


//Represent compass directions

enum CompassDir:

  def clockwise = Vector(North, NorthEast, East, SouthEast, South, SouthWest, West, NorthWest)

  def clockwiseHalf = Vector(North, NorthEast, East, SouthEast, South)

  def counterClockwise = Vector(North, NorthWest, West, SouthWest, South, SouthEast, East, NorthEast)

  def counterClockwiseHalf = Vector(North, NorthWest, West, SouthWest, South)
  
  case North extends CompassDir
  case NorthEast extends CompassDir
  case East extends CompassDir
  case SouthEast extends CompassDir
  case South extends CompassDir
  case SouthWest extends CompassDir
  case West extends CompassDir
  case NorthWest extends CompassDir
 
  def neighborDirections =
        Map(
       North -> Vector(NorthEast, NorthWest),
       NorthEast -> Vector(North, East),
       East ->Vector(NorthEast, SouthEast),
       SouthEast -> Vector(East, South),
       South -> Vector(SouthEast, SouthWest),
       SouthWest -> Vector(West, South),
       West -> Vector(SouthWest, NorthWest),
       NorthWest -> Vector(North, West))(this)



