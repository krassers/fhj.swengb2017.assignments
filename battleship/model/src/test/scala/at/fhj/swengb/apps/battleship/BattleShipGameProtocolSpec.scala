package at.fhj.swengb.apps.battleship

import at.fhj.swengb.apps.battleship.BattleShipProtobuf.Game.Direction
import at.fhj.swengb.apps.battleship.model._
import at.fhj.swengb.apps.battleship.BattleShipProtocol
import org.scalacheck.{Gen, Prop}
import org.scalatest.WordSpecLike
import org.scalatest.prop.Checkers
import scala.collection.JavaConverters._

class BattleShipGameProtocolSpec extends WordSpecLike {


  "BattleShipProtocol" should {
    /*"be deserializable" in {
      Checkers.check(Prop.forAll(battleShipGameGen) {
        expected: BattleShipGame => {
          val actual = BattleShipProtocol.convert(BattleShipProtocol.convert(expected))
          actual == expected
        }
      })
    }*/
    ".convert(pos : BattlePos)" in {
      val expectedPos = BattlePos(4711, 815)
      val actualPos: BattleShipProtobuf.Game.BattlePos = BattleShipProtocol.convert(expectedPos)

      assert(actualPos.getX == expectedPos.x)
      assert(actualPos.getY == expectedPos.y)
    }

    ".convert(vessel : Vessel)" in {
      val expectedVessel = Vessel(NonEmptyString("Peter"), BattlePos(0,0), Horizontal, 2)
      val actualVessel : BattleShipProtobuf.Game.Vessel = BattleShipProtocol.convert(expectedVessel)

      val direction = {
        actualVessel.getDirection match {
          case Direction.Horizontal => Horizontal
          case Direction.Vertical => Vertical
        }
      }

      assert(actualVessel.getStartPos.getX == expectedVessel.startPos.x)
      assert(actualVessel.getStartPos.getY == expectedVessel.startPos.y)

      assert(direction == expectedVessel.direction)

      assert(actualVessel.getName == expectedVessel.name.value)

      assert(actualVessel.getSize == expectedVessel.size)
    }

    ".convert(fleet : Fleet)" in {
      val expectedFleet = Fleet(Set(Vessel(NonEmptyString("Peter"), BattlePos(0,0), Horizontal, 2),
                                    Vessel(NonEmptyString("Hans"), BattlePos(6,4), Vertical, 4)))

      val actualFleet : BattleShipProtobuf.Game.Fleet = BattleShipProtocol.convert(expectedFleet)

      assert(expectedFleet == BattleShipProtocol.convert(actualFleet))
    }

    ".convert(battlefield : BattleField)" in {
      val expectedBattlefield = BattleField(30, 30, Fleet(Set(Vessel(NonEmptyString("Peter"), BattlePos(0,0), Horizontal, 2),
                                                              Vessel(NonEmptyString("Hans"), BattlePos(6,4), Vertical, 4))))

      val actualBattlefield : BattleShipProtobuf.Game.BattleField = BattleShipProtocol.convert(expectedBattlefield)

      assert(expectedBattlefield.width == actualBattlefield.getWidth)
      assert(expectedBattlefield.height == actualBattlefield.getHeight)
      assert(expectedBattlefield.fleet == BattleShipProtocol.convert(actualBattlefield.getFleet))
    }

    ".convert(battleshipgame : BattleShipGame)" in {
      val battlefield = BattleField(30, 30, Fleet(FleetConfig.Standard))
      val expectedGame = BattleShipGame(battlefield, (x : Int) => x.toDouble, (x:Int) => x.toDouble, x => (), "PlayerA")

      val actualGame : BattleShipProtobuf.Game.BattleShipGame = BattleShipProtocol.convert(expectedGame)

      assert(expectedGame.battleField == BattleShipProtocol.convert(actualGame.getBattlefield))
    }
  }
}
