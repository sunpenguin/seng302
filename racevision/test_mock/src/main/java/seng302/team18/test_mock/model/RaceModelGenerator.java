package seng302.team18.test_mock.model;

import seng302.team18.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afj19 on 10/07/17.
 */
public class RaceModelGenerator {



    /**
     * Generate a list of boundarymarks.
     * @return a list boundarymarks
     */
    private List<BoundaryMark> generateBoundaryMarks() {
        boundaryMarks.add(new BoundaryMark(1, new Coordinate(32.31056, -64.84599)));
        boundaryMarks.add(new BoundaryMark(2, new Coordinate(32.30125, -64.82783)));
        boundaryMarks.add(new BoundaryMark(3, new Coordinate(32.28718, -64.83796)));
        boundaryMarks.add(new BoundaryMark(4, new Coordinate(32.28108, -64.85023)));
        boundaryMarks.add(new BoundaryMark(5, new Coordinate(32.29022, -64.86268)));
        boundaryMarks.add(new BoundaryMark(6, new Coordinate(32.30510, -64.85530)));

        return boundaryMarks;
    }


    /**
     * Generate a list of markroundings.
     * @return a list of markroundings
     */
    private List<MarkRounding> generateMarkRoundings() {
        for (int i = 1; i < 8; i ++) {
            markRoundings.add(new MarkRounding(i, compoundMarks.get(i - 1)));
        }

        return markRoundings;
    }


    /**
     * Generate a list of compoundmarks.
     * @return a list of compoundmarks
     */
    private List<CompoundMark> generateCompoundMarks() {
        Mark mark1 = new Mark(131, new Coordinate(32.298577, -64.854304));
        Mark mark2 = new Mark(132, new Coordinate(32.295, -64.85600));
        CompoundMark compoundMark1 = generateCompoundMark("Start/Finish Line", mark1, mark2, 11);
        compoundMarks.add(compoundMark1);

        Mark mark3 = new Mark(133, new Coordinate(32.29651, -64.8514));
        List<Mark> marks = new ArrayList<>();
        marks.add(mark3);
        CompoundMark compoundMark2 = new CompoundMark("Mark", marks, 12);
        compoundMarks.add(compoundMark2);

        Mark mark4 = new Mark(134, new Coordinate(32.30069, -64.83325));
        Mark mark5 = new Mark(135, new Coordinate(32.30269, -64.83325));
        CompoundMark compoundMark3 = generateCompoundMark("EastGate", mark4, mark5, 13);
        compoundMarks.add(compoundMark3);

        Mark mark6 = new Mark(136, new Coordinate(32.28543, -64.85178));
        Mark mark7 = new Mark(137, new Coordinate(32.28551, -64.84766));
        CompoundMark compoundMark4 = generateCompoundMark("SouthGate", mark6, mark7, 14);
        compoundMarks.add(compoundMark4);

        Mark mark8 = new Mark(137, new Coordinate(32.30938, -64.84603));
        Mark mark9 = new Mark(138, new Coordinate(32.30771, -64.84105));
        CompoundMark compoundMark5 = generateCompoundMark("NorthGate", mark8, mark9, 15);
        compoundMarks.add(compoundMark5);

        compoundMarks.add(compoundMark2);
        compoundMarks.add(compoundMark1);

        return compoundMarks;
    }


    /**
     * Generate a compoundmark.
     * @param name String, compoundmark name
     * @param m1 Mark, first mark
     * @param m2 Mark, second mark
     * @param id Integer, compoundmark id
     * @return a compoundmark
     */
    private CompoundMark generateCompoundMark(String name, Mark m1, Mark m2, int id) {
        List<Mark> marks = new ArrayList<>();
        marks.add(m1);
        marks.add(m2);

        CompoundMark compoundMark = new CompoundMark(name, marks, id);

        return compoundMark;
    }
}
