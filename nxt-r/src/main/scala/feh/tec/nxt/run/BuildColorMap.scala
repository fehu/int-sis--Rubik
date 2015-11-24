package feh.tec.nxt.run

import feh.tec.nxt.RubikCubeImage.ColorMapCreationError
import feh.tec.nxt._
import feh.tec.nxt.run.RobotConfig.Default._
import feh.tec.rubik.RubikCube.SideName
import feh.util._
import feh.util.file._

object BuildColorMap extends App with ColorStats{

  var blink_? = false

  if (!blink_?) ls.setFloodlight(true)

  def plotsDir = AbsolutePath(sys.props("user.dir")) / "plots"

  def filePrefix = ""

  val RubikCubeImage(sides) = RubikCubeImage.readSomeImage(gatherColor(blink_?))

  val minMax = colors.mapValues(vs => (vs.min, vs.max))
    .toSeq
    .sortBy(_._2._1)

  boxplot()

  val ranges = Y[(List[(SideName, (Int, Int))], Int), List[(SideName, (Int, Int))]](
    rec => {
      case ((side, (min1, max1)) :: (s2@(_, (min2, max2))) :: tail, prevMax) =>
        if (max1 >= min2) throw ColorMapCreationError("some color domains intersect") // prevMax >= min1
        val minLim = prevMax
        val maxLim = (min2 + max1) / 2
        (side, minLim -> maxLim) :: rec((s2 :: tail, maxLim))
        case ((side, (min1, max1)) :: Nil, prevMax) =>
          (side, prevMax -> Int.MaxValue) :: Nil
    }
  )(minMax.toList -> 0)

  val rangesStr =
    "strictly" + ranges
      .map{ case (side, (min, max)) => Seq(min, max, side).mkString(", ") }
      .mkString("\n", "\n", "\n")

  println(rangesStr)

  args.headOption.map(new File(_)).map{
    _.withOutputStream(File.write.utf8(rangesStr))
  }

  sys.exit(0)
}

/*

Red:    256 255 265 258 258 262 | 255-262
Blue:   151 150 151 153 154 145 | 145-154
Green:  158 157 164 178 160     | 157-178
Yellow: 273 275 278 262 270     | 262-278
Orange: 251 251                 | 251-251
White:  264 254 265             | 254-265

 */

/*

Vector((451,460),              (394,403),              (571,592),              (440,553),              (479,503),              (437,451))
Vector((292,388,(352.4,31.5)), (287,339,(305.1,17.5)), (478,545,(506.2,24.0)), (462,523,(490.3,22.0)), (328,436,(379.0,32.7)), (252,361,(316.7,32.7)))
Vector((298,376,(350.0,26.3)), (290,340,(306.0,17.1)), (472,540,(504.2,25.3)), (463,527,(496.3,21.4)), (1,433,(373.1,31.4)),   (234,379,(308.7,49.4)))
Vector((281,382,(347.7,34.3)), (283,340,(307.8,17.6)), (473,546,(504.8,26.9)), (475,524,(494.7,19.2)), (381,430,(396.8,17.8)), (264,353,(317.2,30.4)))

Vector((432,440,(435.2,2.7)),  (373,406,(382.0,11.3)), (558,567,(563.4,2.7)),  (374,391,(379.8,5.8)),  (498,508,(503.1,3.0)),  (403,427,(412.9,7.7)))
Vector((422,446,(428.8,8.0)),  (368,379,(371.2,4.0)),  (523,541,(529.6,6.1)),  (355,378,(365.1,7.7)),  (465,483,(472.4,4.8)),  (369,387,(379.1,5.2)))
Vector((422,433,(427.3,3.2)),  (370,382,(373.0,4.4)),  (524,544,(534.5,7.0)),  (355,372,(363.0,6.1)),  (469,481,(474.4,3.4)),  (370,399,(384.3,8.9)))

Vector((424,474,(443.8,15.4)), (520,538,(530.7,5.6)), (513,529,(520.7,4.6)), (379,462,(413.7,26.0)), (468,491,(475.8,7.6)), (386,448,(408.3,20.0)))
Vector((418,435,(425.4,4.8)),  (521,547,(535.7,7.5)), (520,538,(529.9,5.9)), (348,362,(354.2,4.5)),  (466,483,(474.2,5.4)), (359,376,(367.8,5.8)))
Vector((387,422,(402.5,9.8)),  (474,501,(489.3,7.2)), (477,491,(483.5,5.8)), (331,390,(353.0,21.1)), (428,452,(437.0,7.4)), (337,403,(358.1,22.1)))


Vector((576,588,(582,4)), (575,581,(578,2)), (574,585,(578,4)), (554,562,(557,3)), (406,424,(418,6)), (396,422,(409,8)))
Vector((580,589,(584,3)), (578,588,(582,3)), (577,588,(580,4)), (555,566,(559,3)), (409,425,(418,5)), (395,423,(409,9)))
Vector((442,448,(445,2)), (557,584,(574,7)), (571,577,(574,3)), (377,410,(394,10)), (493,502,(499,3)), (398,415,(404,6)))

 */