package org.grobid.core.engines;


import com.google.common.collect.Lists;
import fr.limsi.wapiti.SWIGTYPE_p_mdl_t;
import fr.limsi.wapiti.Wapiti;
import org.apache.commons.lang3.tuple.Pair;
import org.grobid.core.GrobidModels;
import org.grobid.core.data.BibDataSet;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.data.Date;
import org.grobid.core.data.Person;
import org.grobid.core.document.Document;
import org.grobid.core.engines.citations.LabeledReferenceResult;
import org.grobid.core.engines.counters.CitationParserCounters;
import org.grobid.core.engines.tagging.WapitiTagger;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.factory.GrobidFactory;
import org.grobid.core.features.FeaturesVectorDate;
import org.grobid.core.jni.WapitiModel;
import org.grobid.core.main.LibraryLoader;
import org.grobid.core.utilities.TextUtilities;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

@Ignore
public class EngineTest {

    @BeforeClass
    public static void init() {
        LibraryLoader.load();
    }

    @Test
    public void testGetNewModel() {
        // assertEquals("Wrong value of getModel", "-m "+GrobidModels.CITATION.getModelPath()+" ", GrobidModels.CITATION.getModelPath());
    }


    @Test
    public void testWapiti() {
        String s = "References references R Re Ref Refe s es ces nces LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "1 1 1 1 1 1 1 1 1 1 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "Bar bar B Ba Bar Bar r ar Bar Bar LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 1 <reference-block>\n" +
                "Haim haim H Ha Hai Haim m im aim Haim LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Dolan dolan D Do Dol Dola n an lan olan LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Ferro ferro F Fe Fer Ferr o ro rro erro LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "L l L L L L L L L L LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Giampiccolo giampiccolo G Gi Gia Giam o lo olo colo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Szpektor szpektor S Sz Szp Szpe r or tor ktor LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Second second S Se Sec Seco d nd ond cond LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEEND ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Second second S Se Sec Seco d nd ond cond LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Challenges challenges C Ch Cha Chal s es ges nges LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "on on o on on on n on on on LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "Venice venice V Ve Ven Veni e ce ice nice LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "Italy italy I It Ita Ital y ly aly taly LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "2 2 2 2 2 2 2 2 2 2 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "Bunescu bunescu B Bu Bun Bune u cu scu escu LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Mooney mooney M Mo Moo Moon y ey ney oney LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "Subsequence subsequence S Su Sub Subs e ce nce ence LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Kernels kernels K Ke Ker Kern s ls els nels LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Relation relation R Re Rel Rela n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Extraction extraction E Ex Ext Extr n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Advances advances A Ad Adv Adva s es ces nces LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "in in i in in in n in in in LINEIN NOCAPS NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Neural neural N Ne Neu Neur l al ral ural LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Processing processing P Pr Pro Proc g ng ing sing LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "18 18 1 18 18 18 8 18 18 18 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "MIT mit M MI MIT MIT T IT MIT MIT LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Press press P Pr Pre Pres s ss ess ress LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "3 3 3 3 3 3 3 3 3 3 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "Glickman glickman G Gl Gli Glic n an man kman LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "O o O O O O O O O O LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Quiñonero quiñonero Q Qu Qui Quiñ o ro ero nero LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "- - - - - - - - - - LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 3 <reference-block>\n" +
                "Candela candela C Ca Can Cand a la ela dela LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "et et e et et et t et et et LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "al al a al al al l al al al LINEIN NOCAPS NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "editors editors e ed edi edit s rs ors tors LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "MLCW mlcw M ML MLC MLCW W CW LCW MLCW LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "LNAI lnai L LN LNA LNAI I AI NAI LNAI LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Volume volume V Vo Vol Volu e me ume lume LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "3944 3944 3 39 394 3944 4 44 944 3944 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "177 177 1 17 177 177 7 77 177 177 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "190 190 1 19 190 190 0 90 190 190 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Springer springer S Sp Spr Spri r er ger nger LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "Verlag verlag V Ve Ver Verl g ag lag rlag LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "4 4 4 4 4 4 4 4 4 4 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Jenny jenny J Je Jen Jenn y ny nny enny LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Rose rose R Ro Ros Rose e se ose Rose LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Finkel finkel F Fi Fin Fink l el kel nkel LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "Trond trond T Tr Tro Tron d nd ond rond LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Grenager grenager G Gr Gre Gren r er ger ager LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Christopher christopher C Ch Chr Chri r er her pher LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Manning manning M Ma Man Mann g ng ing ning LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Incorporating incorporating I In Inc Inco g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Non non N No Non Non n on Non Non LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "local local l lo loc loca l al cal ocal LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "into into i in int into o to nto into LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Extraction extraction E Ex Ext Extr n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "by by b by by by y by by by LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Gibbs gibbs G Gi Gib Gibb s bs bbs ibbs LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Sampling sampling S Sa Sam Samp g ng ing ling LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "43nd 43nd 4 43 43n 43nd d nd 3nd 43nd LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Annual annual A An Ann Annu l al ual nual LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Meeting meeting M Me Mee Meet g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Association association A As Ass Asso n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "for for f fo for for r or for for LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Linguistics linguistics L Li Lin Ling s cs ics tics LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "( ( ( ( ( ( ( ( ( ( LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 OPENBRACKET 5 <reference-block>\n" +
                "ACL acl A AC ACL ACL L CL ACL ACL LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ") ) ) ) ) ) ) ) ) ) LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 ENDBRACKET 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "pp pp p pp pp pp p pp pp pp LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "363 363 3 36 363 363 3 63 363 363 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 5 <reference-block>\n" +
                "370 370 3 37 370 370 0 70 370 370 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "5 5 5 5 5 5 5 5 5 5 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "Giampiccolo giampiccolo G Gi Gia Giam o lo olo colo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Dolan dolan D Do Dol Dola n an lan olan LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Third third T Th Thi Thir d rd ird hird LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEEND INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Paraphrasing paraphrasing P Pa Par Para g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "1 1 1 1 1 1 1 1 1 1 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "9 9 9 9 9 9 9 9 9 9 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "Prague prague P Pr Pra Prag e ue gue ague LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "June june J Ju Jun June e ne une June LINEIN INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 6 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "6 6 6 6 6 6 6 6 6 6 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "Gildea gildea G Gi Gil Gild a ea dea ldea LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Palmer palmer P Pa Pal Palm r er mer lmer LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "M m M M M M M M M M LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Necessity necessity N Ne Nec Nece y ty ity sity LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Parsing parsing P Pa Par Pars g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Predicate predicate P Pr Pre Pred e te ate cate LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Argument argument A Ar Arg Argu t nt ent ment LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Recognition recognition R Re Rec Reco n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "40th 40th 4 40 40t 40th h th 0th 40th LINESTART NOCAPS CONTAINSDIGITS 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Meeting meeting M Me Mee Meet g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Association association A As Ass Asso n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Linguistics linguistics L Li Lin Ling s cs ics tics LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "( ( ( ( ( ( ( ( ( ( LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 OPENBRACKET 7 <reference-block>\n" +
                "ACL acl A AC ACL ACL L CL ACL ACL LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ") ) ) ) ) ) ) ) ) ) LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 ENDBRACKET 7 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 7 <reference-block>\n" +
                "239 239 2 23 239 239 9 39 239 239 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 7 <reference-block>\n" +
                "246 246 2 24 246 246 6 46 246 246 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "Philadelphia philadelphia P Ph Phi Phil a ia hia phia LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "PA pa P PA PA PA A PA PA PA LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "7 7 7 7 7 7 7 7 7 7 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "Lin lin L Li Lin Lin n in Lin Lin LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "1998 1998 1 19 199 1998 8 98 998 1998 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "Dependency dependency D De Dep Depe y cy ncy ency LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 7 <reference-block>\n" +
                "based based b ba bas base d ed sed ased LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Evaluation evaluation E Ev Eva Eval n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "MINIPAR minipar M MI MIN MINI R AR PAR IPAR LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Evaluation evaluation E Ev Eva Eval n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Parsing parsing P Pa Par Pars g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "8 8 8 8 8 8 8 8 8 8 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Piskorski piskorski P Pi Pis Pisk i ki ski rski LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "J j J J J J J J J J LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "A a A A A A A A A A LINEIN ALLCAP NODIGIT 1 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Shallow shallow S Sh Sha Shal w ow low llow LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Text text T Te Tex Text t xt ext Text LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Processing processing P Pr Pro Proc g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Core core C Co Cor Core e re ore Core LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Engine engine E En Eng Engi e ne ine gine LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Journal journal J Jo Jou Jour l al nal rnal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Intelligence intelligence I In Int Inte e ce nce ence LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "Volume volume V Vo Vol Volu e me ume lume LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "18 18 1 18 18 18 8 18 18 18 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "Number number N Nu Num Numb r er ber mber LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "3 3 3 3 3 3 3 3 3 3 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "451 451 4 45 451 451 1 51 451 451 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 8 <reference-block>\n" +
                "476 476 4 47 476 476 6 76 476 476 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "9 9 9 9 9 9 9 9 9 9 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Anselmo anselmo A An Ans Anse o mo lmo elmo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Peñas peñas P Pe Peñ Peña s as ñas eñas LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "Álvaro álvaro Á Ál Álv Álva o ro aro varo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Rodrigo rodrigo R Ro Rod Rodr o go igo rigo LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "Felisa felisa F Fe Fel Feli a sa isa lisa LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Verdejo verdejo V Ve Ver Verd o jo ejo dejo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Overview overview O Ov Ove Over w ew iew view LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Answer answer A An Ans Answ r er wer swer LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Validation validation V Va Val Vali n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Exercise exercise E Ex Exe Exer e se ise cise LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "CLEF clef C CL CLE CLEF F EF LEF CLEF LINEEND ALLCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Working working W Wo Wor Work g ng ing king LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Notes notes N No Not Note s es tes otes LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "10 10 1 10 10 10 0 10 10 10 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "2007a 2007a 2 20 200 2007 a 7a 07a 007a LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "a a a a a a a a a a LINEIN NOCAPS NODIGIT 1 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Subsequence subsequence S Su Sub Subs e ce nce ence LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Kernel kernel K Ke Ker Kern l el nel rnel LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Method method M Me Met Meth d od hod thod LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Proc proc P Pr Pro Proc c oc roc Proc LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "of of o of of of f of of of LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "AAAI aaai A AA AAA AAAI I AI AAI AAAI LINESTART ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "11 11 1 11 11 11 1 11 11 11 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "2007b 2007b 2 20 200 2007 b 7b 07b 007b LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Sentence sentence S Se Sen Sent e ce nce ence LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Similarity similarity S Si Sim Simi y ty ity rity LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "based based b ba bas base d ed sed ased LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Dependency dependency D De Dep Depe y cy ncy ency LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Tree tree T Tr Tre Tree e ee ree Tree LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Skeletons skeletons S Sk Ske Skel s ns ons tons LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Paraphrasing paraphrasing P Pa Par Para g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "36 36 3 36 36 36 6 36 36 36 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "41 41 4 41 41 41 1 41 41 41 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "Prague prague P Pr Pra Prag e ue gue ague LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "June june J Ju Jun June e ne une June LINEEND INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "12 12 1 12 12 12 2 12 12 12 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "2007c 2007c 2 20 200 2007 c 7c 07c 007c LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "DFKI dfki D DF DFK DFKI I KI FKI DFKI LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "LT lt L LT LT LT T LT LT LT LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "at at a at at at t at at at LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "AVE ave A AV AVE AVE E VE AVE AVE LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 11 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Answer answer A An Ans Answ r er wer swer LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Validation validation V Va Val Vali n on ion tion LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "online online o on onl onli e ne ine line LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "proceedings proceedings p pr pro proc s gs ngs ings LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "CLEF clef C CL CLE CLEF F EF LEF CLEF LINEIN ALLCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Working working W Wo Wor Work g ng ing king LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Notes notes N No Not Note s es tes otes LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "ISBN isbn I IS ISB ISBN N BN SBN ISBN LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 12 <reference-block>\n" +
                "2 2 2 2 2 2 2 2 2 2 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "912335 912335 9 91 912 9123 5 35 335 2335 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "31 31 3 31 31 31 1 31 31 31 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "0 0 0 0 0 0 0 0 0 0 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "September september S Se Sep Sept r er ber mber LINEIN INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 12 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "Budapest budapest B Bu Bud Buda t st est pest LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "Hungary hungary H Hu Hun Hung y ry ary gary LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 12 <reference-block>\n" +
                "\n";

        s = s + s + s + s;
//        Engine engine = GrobidFactory.getInstance().getEngine();
//        WapitiTagger t = new WapitiTagger(GrobidModels.REFERENCE_SEGMENTER);

        SWIGTYPE_p_mdl_t mod = Wapiti.loadModel("label -m /Work/workspace/grobid-rg/grobid-home/models/reference-segmenter/model.wapiti");

        for (int i = 0; i < 1000000; i++) {
            if (i % 100 == 0) {
                System.out.println("Processed: " + i);
            }
            Wapiti.labelFromModel(mod, s);
        }
    }

    private static String getDateStr(String input) throws Exception {
        if (input == null)
            return null;

        ArrayList<String> dateBlocks = new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(input, "([" + TextUtilities.punctuations, true);

        if (st.countTokens() == 0)
            return null;
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (!tok.equals(" ")) {
                dateBlocks.add(tok + " <date>");
            }
        }

        return FeaturesVectorDate.addFeaturesDate(dateBlocks);

    }


    private void testWap(final String forTest, File modelFile) throws InterruptedException {
        final WapitiModel wm = new WapitiModel(modelFile);
        String res;

        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        String res = wm.label(forTest);
                        System.out.println("RES: " + res.trim());
                    }
                }
            };
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }


        wm.close();
    }

    @Test
    public void testDateParser() throws Exception {

        String d = "12 August, 1985";
        List<Date> processing = new DateParser().processing(d);
        System.out.println(processing);
    }

    @Test
    public void testPDF() throws Exception {
        Engine engine = GrobidFactory.getInstance().getEngine();
        BiblioItem resHeader = new BiblioItem();
        engine.getParsers().getHeaderParser().processing("//Work/temp/1.pdf", false, resHeader);
        System.out.println(resHeader);
        System.out.println(engine.fullTextToTEI("//Work/temp/1.pdf", false, false));

    }

    @Test
    public void testEmailPDF() throws Exception {
        Engine engine = GrobidFactory.getInstance().getEngine();
        BiblioItem resHeader = new BiblioItem();
        engine.getParsers().getHeaderParser().processing("/Work/temp/1.pdf", false, resHeader);
        System.out.println(resHeader);
//        System.out.println(engine.fullTextToTEI("/tmp/2.pdf", false, false));

    }


    /*@Test
    public void stress() throws Exception {
        for (int i = 0; i < 1000000; i++) {
            testReferenceSegmenter();
        }
    }*/

    @Test
    public void extractCitationsFromPDF() throws Exception {
        Engine engine = GrobidFactory.getInstance().getEngine();
//        String input = "/Work/workspace/data/pdf2xmlreflow/1.pdf";
//        String input = "/Users/zholudev/Downloads/ttt.pdf";
//        String input = "/Users/zholudev/Downloads/stem.pdf";
//        String input = "/Work/workspace/data/elsevier_pdfs/8.pdf";
//        String input = "/tmp/1.pdf";
//        for (int i = 0; i < 10000; i++) {
//        String input = "/Work/workspace/pdf-analysis/pdf-analysis-service/scripts/grobid/pdfs/grobid-input-1072141691733992581.pdf";
//        String input = "/Work/workspace/pdf-analysis/pdf-analysis-service/scripts/grobid/pdfs/grobid-input-2086711400313078388.pdf";
//        String input = "/Work/workspace/pdf-analysis/pdf-analysis-service/scripts/grobid/AS_190528951947270_1422437050969.pdf";
        String input = "/Work/temp/1.pdf";
        Document doc = engine.getParsers().getSegmentationParser().processing(input);
        System.out.println("Extracting citations");
        List<BibDataSet> cits = engine.getParsers().getCitationParser().processingReferenceSection(doc, engine.getParsers().getReferenceSegmenterParser(), false);
        for (BibDataSet c : cits) {
            System.out.println(c.getResBib().getTitle() + "--->" + c.getResBib().getAuthors());
        }
        System.out.println("CITATIONS: " + cits.size());
//        }

    }

    @Test
    public void testSegmentation() throws Exception {
        Engine engine = GrobidFactory.getInstance().getEngine();
        Document result = engine.getParsers().getSegmentationParser().processing("/Work/workspace/data/pdf2xmlreflow/1.pdf");
        System.out.println(result);

    }




/*    @Test
    public void testAuthorExtraction() throws Exception {
        Engine engine = GrobidFactory.getInstance().getEngine();
        String a = "Amelia Kenner Brininger, MPH, CPH , Emergency Medical Services, County of San Diego, San Diego, CA\n" +
                "Barbara M. Stepanski, MPH , Emergency Medical Services, County of San Diego Health and Human Services Agency, San Diego, CA\n" +
                "Diane Royer, RN, BSN , County of San Diego, Emergency Medical Services, San Diego, CA\n" +
                "Bruce Haynes, MD , Emergency Medical Services, County of San Diego Health and Human Services Agency, San Diego, CA\n" +
                "Leslie Ray, MPH, MPPA, MA , Emergency Medical Services, County of San Diego Health and Human Services Agency, San Diego, CA\n" +
                "Sanaa Abedin, MPH , Community Health Statistics Unit, Health Care Policy Administration, County of San Diego Health and Human Services Agency, San Diego, CA\n" +
                "Alicia Sampson, MPH, CPH , Health & Human Services Agency, Public Health Services, County of San Diego, San Diego, CA\n" +
                "Joshua Smith, PhD, MPH , Emergency Medical Services, County of San Diego Health and Human Services Agency, San Diego, CA\n" +
                "Isabel Corcos, PhD, MPH , County of San Diego, Emergency Medical Services, County of San Diego, San Diego, CA\n" +
                "Ryan Smith, MPH , Emergency Medical Services, County of San Diego, San Diego, CA";

//        a = "M. Yoshida, T. Yomogida, T. Mineo (Keio University), K. Nitta, K. Kato (JASRI), T. Masuda (National Institute for Materials Science), H. Nitani, H. Abe (KEK), S. Takakusagi (Hokkaido University), T. Uruga (JASRI/SPring-8), K. Asakura (Hokkaido University), K. Uosaki (National Institute for Materials Science), and H. Kondoh (Keio University)";

        List<LabeledReferenceResult> references = engine.getParsers().getReferenceSegmenterParser().extract(a);

        BiblioItem res = engine.getParsers().getCitationParser().processing("Amelia Kenner Brininger, MPH, CPH , Emergency Medical Services, County of San Diego, San Diego, CA", false);

        System.out.println(res);
//        List<BibDataSet> results = Lists.newArrayList();
//        for (LabeledReferenceResult ref : references) {
//            BiblioItem bib = engine.getParsers().getCitationParser().processing(ref.getReferenceText(), false);
//            BibDataSet bds = new BibDataSet();
//            bds.setResBib(bib);
//            bds.setRefSymbol(ref.getLabel());
//            bds.setRawBib(ref.getReferenceText());
//            results.add(bds);
//        }

        List<Person> authors = engine.getParsers().getAuthorParser().processing(Arrays.asList(a.split("\n")), false);

        for (Person p : authors) {
            System.out.println(p);
        }

        authors = engine.getParsers().getAuthorParser().processing(Arrays.asList(a.split("\n")), false);
        System.out.println("+++++++++++++++++++++");
        for (Person p : authors) {
            System.out.println(p);
        }


//        for (Object r : results) {
//            System.out.println(r);
//        }

//        Pair<String, Document> result = engine.getParsers().getHeaderParser().pro
//        BiblioItem res =
//                engine.getParsers().getCitationParser().processingReferenceSection(a, false);
//        System.out.println("--------------------");
////        for (Person p : res) {
////            System.out.println(p);
////        }
//        System.out.println(res);

    }*/

    /*@Test
    public void testReferenceSegmenter() throws Exception {
        String block = "Adelman, J. S., Marquis, S. J., & Sabatos-DeVito, M. G. (2010). Letters in words are read simultaneously, not in left-to-right sequence. Psychological Science, 21, 1799–1801. Arditi, A., Knoblauch, K., & Grunwald, I. (1990). Reading with ﬁxed and variable character pitch. Journal of the Optical Society of America, 7, 2011–2015. Bernard, J. -B., & Chung, S. T. L. (2011). The dependence of crowding on ﬂanker complex- ity and target–ﬂanker similarity. Journal of Vision, 11(8), 1–16 (1). Chanceaux, M., & Grainger, J. (2012). Serial position effects in the identiﬁcation of letters, digits, symbols, and shapes in peripheral vision. Acta Psychologica, 141, 149–158. Chanceaux, M., Mathôt, S., & Grainger, J. (2013). Flank to the left, ﬂank to the right: Testing the modiﬁed receptive ﬁeld hypothesis of letter-speciﬁc crowding. Journal of Cognitive Psychology, 25, 774–780. Chung, S. T. L. (2002). The effect of letter spacing on reading speed in central and periph- eral vision. Investigative Ophthalmology & Visual Science, 43, 1270–1276. Grainger, J. (2008). Cracking the orthographic code: An introduction. Language and Cognitive Processes, 23, 1–35. Grainger, J., Tydgat, I., & Isselé, J. (2010). Crowding affects letters and symbols differ- ently. Journal of Experimental Psychology: Human Perception and Performance, 36, 673–688. Grainger, J., & Van Heuven, W. (2003). Modeling letter position coding in printed word perception. In P. Bonin (Ed.), The mental lexicon (pp. 1–24). New York: Nova Science Publishers. Johnson, R. L., & Eisler, M. E. (2012). The importance of the ﬁrst and last letter in words during sentence reading. Acta Psychologica, 141, 336–351.\n" +
                "\n" +
                "Legge, G. E., Pelli, D.G., Rubin, G. S., & Schleske, M. M. (1985). Psychophysics of reading. I. Normal vision. Vision Research, 25, 239–252. Perea, M., Moret-Tatay, C., & Gomez, P. (2011). The effects of inter letter spacing in visual-word recognition. Acta Psychologica, 137, 345–351. Scaltritti, M., & Balota, D. A. (2013). Are all letters processed equally in parallel? Further evidence of a robust ﬁrst-letter advantage. Acta Psychologica, 144, 397–410. Stevens, M., & Grainger, J. (2003). Letter visibility and the viewing position effect in visual word recognition. Perception & Psychophysics, 65, 133–151. Tripathy, S. P., & Cavanagh, P. (2002). The extent of crowding in peripheral vision does not scale with target size. Vision Research, 42, 2357–2369. Tripathy, S., Cavanagh, P., & Bedell, H. (2013, May). Large interaction zones for visual crowding for brieﬂy presented peripheral stimuli. Poster session presented at 13th Annual Meeting of Vision Science Society, Naples, Florida. Tydgat, I., & Grainger, J. (2009). Serial position effects in the identiﬁcation of letters, digits, and symbols. Journal of Experimental Psychology: Human Perception and Performance, 35, 480–498. Vinkcier, F., Qiao, E., Pallier, C., Dehaene, S., & Cohen, L. (2011). The impact of letter spacing on reading: A test of the bigram coding hypothesis. Journal of Vision, 11, 1–21. Whitney, C. (2008). Supporting the serial in the SERIOL model. Language & Cognitive Processes, 23, 824–865. Yu, D., Cheung, S. -H., Legge, G. E., & Chung, S. T. L. (2007). Effect of letter spacing on visual span and reading speed. Journal of Vision, 7, 1–10. Zorzi, M., Barbiero, C., Facoetti, A., Lonciari, L., Carrozzi, M., Montico, M., et al. (2012). Extra-large letter spacing improves reading in dyslexia. Proceedings of the National Academy of Sciences, 109, 11455–11459.";

        block = "[1] C.P. Wild, Environmental exposure measurement in cancer epidemiology, Mutagenesis 24 (2009) 117–125. \n" +
                "[2] G.N. Wogan, T.W. Kensler, J.D. Groopman, Present and future directions of translational research on aﬂatoxin and hepatocellular carcinoma. A review, Food Addit. Contam. Part A: Chem. Anal. Control Expos. Risk Assess. 29 (2012) 249–257. \n" +
                "[3] H.M. Shen, C.N. Ong, Mutations of the p53 tumor suppressor gene and ras onco- genes in aﬂatoxin hepatocarcinogenesis, Mutat. Res. Rev. Genet. Toxicol. 366 (1996) 23–44. \n" +
                "[4] K.A. McGlynn, W.T. London, The global epidemiology of hepatocellular carci- noma: present and future, Clin. Liver Dis. 15 (2011) 223–243. \n" +
                "[5] J.F. Solus, B.J. Arietta, J.R. Harris, D.P. Sexton, J.Q. Steward, C. McMunn, P. Ihrie, J.M. Mehall, T.L. Edwards, E.P. Dawson, Genetic variation in eleven phase I drug metabolism genes in an ethnically diverse population, Pharmacogenomics 5 (2004) 895–931.\n" +
                "\n" +
                "[6] C. Ioannides, D.F. Lewis, Cytochromes P450 in the bioactivation of chemicals, Curr. Top. Med. Chem. 4 (2004) 1767–1788. \n" +
                "[7] T. Omura, Forty years of cytochrome P450, Biochem. Biophys. Res. Commun. 266 (1999) 690–698. \n" +
                "[8] C.N. Martin, R.C. Garner, Aﬂatoxin B1-oxide generated by chemical or enzy- matic oxidation of aﬂatoxin B1 causes guanine substitution in nucleic acids, Nature 267 (1977) 863–865. \n" +
                "[9] M.E. Smela, M.L. Hamm, P.T. Henderson, C.M. Harris, T.M. Harris, J.M. Essig- mann, The aﬂatoxin B(1) formamidopyrimidine adduct plays a major role in causing the types of mutations observed in human hepatocellular carcinoma, PNAS 99 (2002) 6655–6660. \n" +
                "[10] D.W. Nebert, T.P. Dalton, The role of cytochrome P450 enzymes in endoge- nous signalling pathways and environmental carcinogenesis, Nat. Rev. Cancer 6 (2006) 947–960. \n" +
                "[11] A. Gunes, M.L. Dahl, Variation in CYP1A2 activity and its clinical implications: inﬂuence of environmental factors and genetic polymorphisms, Pharmacoge- nomics 9 (2008) 625–637. \n" +
                "[12] T. Shimada, Xenobiotic-metabolizing enzymes involved in activation and detoxiﬁcation of carcinogenic polycyclic aromatic hydrocarbons, Drug Metab. Pharmacokinet. 21 (2006) 257–276. \n" +
                "[13] A.R. Boobis, N.J. Gooderham, K.J. Rich, K. Zhao, R.J. Edwards, B.P. Murray, A.M. Lynch, S. Murray, D.S. Davies, Enzymatic studies of the activation of heterocyclic food mutagens in man, Princess Takamatsu Symp. 23 (1995) 134–144. \n" +
                "[14] D.L. Eaton, E.P. Gallagher, Mechanisms of aﬂatoxin carcinogenesis, Ann. Rev. Pharmacol. Toxicol. 34 (1994) 135–172. \n" +
                "[15] D. Kim, F.P. Guengerich, Cytochrome P450 activation of arylamines and hete- rocyclic amines, Annu. Rev. Pharmacol. Toxicol. 45 (2005) 27–49. \n" +
                "[16] E.P. Gallagher, K.L. Kunze, P.L. Stapleton, D.L. Eaton, The kinetics of aﬂa- toxin B1 oxidation by human cDNA-expressed and human liver microsomal cytochromes P450 1A2 and 3A4, Toxicol. Appl. Pharmacol. 141 (1996) 595–606. \n" +
                "[17] F.P. Guengerich, A. Parikh, R.J. Turesky, P.D. Josephy, Inter-individual differ- ences in the metabolism of environmental toxicants: cytochrome P450 1A2 as a prototype, Mutat. Res. 428 (1999) 115–124. \n" +
                "[18] H.C. Liang, H. Li, R.A. McKinnon, J.J. Duffy, S.S. Potter, A. Puga, D.W. Nebert, Cyp1a2(−/−) null mutant mice develop normally but show deﬁcient drug metabolism, PNAS 93 (1996) 1671–1676. \n" +
                "[19] N. Dragin, S. Uno, B. Wang, T.P. Dalton, D.W. Nebert, Generation of ‘humanized’ hCYP1A1 1A2 Cyp1a1/1a2(−/−) mouse line, Biochem. Biophys. Res. Commun. 359 (2007) 635–642. \n" +
                "[20] M.T. Landi, R. Sinha, N.P. Lang, F.F. Kadlubar, Human cytochrome P4501A2JT IARC Sci. Publ (1999) 173–195. \n" +
                "[21] W. Kalow, B.K. Tang, Caffeine as a metabolic probe: exploration of the enzyme- inducing effect of cigarette smoking, Clin. Pharmacol. Ther. 49 (1991) 44–48. \n" +
                "[22] D.W. Nebert, T.P. Dalton, A.B. Okey, F.J. Gonzalez, Role of aryl hydrocarbon receptor-mediated induction of the CYP1 enzymes in environmental toxicity and cancer, J. Biol. Chem. 279 (2004) 23847–23850. \n" +
                "[23] B.B. Rasmussen, T.H. Brix, K.O. Kyvik, K. Brøsen, The interindividual differences in the 3-demthylation of caffeine alias CYP1A2 is determined by both genetic and environmental factors, Pharmacogenetics 12 (2002) 473–478. \n" +
                "[24] K. Klein, S. Winter, M. Turpeinen, M. Schwab, U.M. Zanger, Pathway-targeted pharmacogenomics of CYP1A2 in human liver, Front. Pharmacol. 1 (2010) 129. \n" +
                "[25] K. Ikeya, A.K. Jaiswal, R.A. Owens, J.E. Jones, D.W. Nebert, S. Kimura, Human CYP1A2: sequence, gene structure, comparison with the mouse and rat orthol- ogous gene, and differences in liver 1A2 mRNA expression, Mol. Endocrinol. 3 (1989) 1399–1408. ";

        block = "References \n" +
                "\n" +
                "1. Bar-Haim, R., Dagan, I., Dolan, B., Ferro, L., Giampiccolo, D., Magnini, B. and Szpektor, I. 2006. The Second PASCAL \n" +
                "Recognising Textual Entailment Challenge. In Proceedings of the Second PASCAL Challenges Workshop on \n" +
                "Recognising Textual Entailment, Venice, Italy. \n" +
                "2. Bunescu, R. and Mooney, R. 2006. Subsequence Kernels for Relation Extraction. In Advances in Neural Information \n" +
                "Processing Systems 18. MIT Press. \n" +
                "3. Dagan, I., Glickman, O., and Magnini, B. 2006. The PASCAL Recognising Textual Entailment Challenge. In Quiñonero-\n" +
                "Candela et al., editors, MLCW 2005, LNAI Volume 3944, pages 177-190. Springer-Verlag. \n" +
                "4. Jenny Rose Finkel, Trond Grenager, and Christopher Manning. 2005. Incorporating Non-local Information into \n" +
                "Information Extraction Systems by Gibbs Sampling. Proceedings of the 43nd Annual Meeting of the Association for \n" +
                "Computational Linguistics (ACL 2005), pp. 363-370. \n" +
                "5. Giampiccolo, D., Magnini, B., Dagan, I., and Dolan, B. 2007. The Third PASCAL Recognizing Textual Entailment \n" +
                "Challenge. In Proceedings of the Workshop on Textual Entailment and Paraphrasing, pages 1–9, Prague, June 2007. \n" +
                "6. Gildea, D. and Palmer, M. 2002. The Necessity of Parsing for Predicate Argument Recognition. In Proceedings of the \n" +
                "40th Meeting of the Association for Computational Linguistics (ACL 2002):239-246, Philadelphia, PA. \n" +
                "7. Lin, D. 1998. Dependency-based Evaluation of MINIPAR. In Workshop on the Evaluation of Parsing Systems. \n" +
                "8. Neumann, G. and Piskorski, J. 2002. A Shallow Text Processing Core Engine. Journal of Computational Intelligence, \n" +
                "Volume 18, Number 3, 2002, pages 451-476. \n" +
                "9. Anselmo Peñas, Álvaro Rodrigo, Felisa Verdejo. 2007. Overview of the Answer Validation Exercise 2007. In the CLEF \n" +
                "2007 Working Notes. \n" +
                "10. Wang, R. and Neumann, G. 2007a. Recognizing Textual Entailment Using a Subsequence Kernel Method. In Proc. of \n" +
                "AAAI 2007. \n" +
                "11. Wang, R. and Neumann, G. 2007b. Recognizing Textual Entailment Using Sentence Similarity based on Dependency \n" +
                "Tree Skeletons. In Proceedings of the Workshop on Textual Entailment and Paraphrasing, pages 36–41, Prague, June \n" +
                "2007. \n" +
                "12. Wang, R. and Neumann, G. 2007c. DFKI–LT at AVE 2007: Using Recognizing Textual Entailment for Answer \n" +
                "Validation. In online proceedings of CLEF 2007 Working Notes, ISBN: 2-912335-31-0, September 2007, Budapest, \n" +
                "Hungary.\n";

//        block = "Jacobsen, S., \n2013. Serum amyloid A and haptoglobin ";
        Engine engine = GrobidFactory.getInstance().getEngine();
        ReferenceSegmenterParser p = new ReferenceSegmenterParser();

        System.out.println("Testing block: " + block);

        for (int i = 0; i < 10000000; i++)
        for (LabeledReferenceResult pair : p.extract(block)) {
            if (pair.getLabel() != null) {
                System.out.println(pair.getLabel() + " ---> " + pair.getReferenceText());
            } else {
                System.out.println("---> " + pair.getReferenceText());
            }
        }

//        System.out.println("Training data:");
//        System.out.println("--------------");
//        System.out.println(p.createTrainingData(block));
//        System.out.println("--------------");
    }*/


    @Test
    public void testFulltext() throws Exception {
        final Engine engine = GrobidFactory.getInstance().getEngine();
        System.out.println(engine.fullTextToTEI("/Work/temp/context/pdf3.pdf", true, true, null, -1, -1, true));
//        System.out.println(engine.fullTextToTEI("/tmp/x1.pdf", true, true, null, -1, -1, true));
    }

    @Test
    public void testReferenceString() {
//        String ref = "Agharahimi, M.R., LeBel, N.A., 1995. Synthesis of (–)-monoterpenylmagnolol and \n" +
//                "magnolol. J. Org. Chem. 60, 1856–1863. ";
        String ref = "30. Van der Veen, J. 1963. The role of adenoviruses in respiratory disease. Am. Rev. Respir. Dis. 88: 167–181.";

        final Engine engine = GrobidFactory.getInstance().getEngine();
        BiblioItem x = engine.processRawReference(ref, false);
        System.out.println(x.getTitle() + "; " + x.getAuthors());
        System.out.println(x.getJournal());
        System.out.println(x.getPublicationDate());
        System.out.println(x);


    }

    @Test
    public void testMultiThreading() throws Exception {
        final Engine engine = GrobidFactory.getInstance().getEngine();
//        String res = engine.fullTextToTEI("/tmp/planetary-moc.pdf", false, false);
//        List<BibDataSet> citRes = engine.processReferences("/tmp/planetary-moc.pdf", false);
//        System.out.println(res);

        final String cit = " M. Kitsuregawa, H. Tanaka, and T. Moto-oka. Application of hash to data base machine and its architecture. New Generation Computing,  1 (1),   1983.";

        long t = System.currentTimeMillis();
        int n = 3;
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    int cnt = 0;
                    for (int i = 0; i < 100; i++) {
                        try {
                            engine.processRawReference(cit, false);
                        } catch (Exception e) {
                            //no op
                        }
                        if (++cnt % 10 == 0) {
                            System.out.println(cnt);
                        }
                    }

                }
            };
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
//            threads[i].join();
        }

        for (int i = 0; i < n; i++) {
            threads[i].join();
        }


        System.out.println("Took ms: " + (System.currentTimeMillis() - t));
    }

    public static void main(String[] args) {
        String s = "References references R Re Ref Refe s es ces nces LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "1 1 1 1 1 1 1 1 1 1 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "Bar bar B Ba Bar Bar r ar Bar Bar LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 1 <reference-block>\n" +
                "Haim haim H Ha Hai Haim m im aim Haim LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Dolan dolan D Do Dol Dola n an lan olan LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Ferro ferro F Fe Fer Ferr o ro rro erro LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "L l L L L L L L L L LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Giampiccolo giampiccolo G Gi Gia Giam o lo olo colo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Szpektor szpektor S Sz Szp Szpe r or tor ktor LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 1 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 1 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 1 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Second second S Se Sec Seco d nd ond cond LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEEND ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 1 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Second second S Se Sec Seco d nd ond cond LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Challenges challenges C Ch Cha Chal s es ges nges LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "on on o on on on n on on on LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "Venice venice V Ve Ven Veni e ce ice nice LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "Italy italy I It Ita Ital y ly aly taly LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "2 2 2 2 2 2 2 2 2 2 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "Bunescu bunescu B Bu Bun Bune u cu scu escu LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Mooney mooney M Mo Moo Moon y ey ney oney LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 2 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "Subsequence subsequence S Su Sub Subs e ce nce ence LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Kernels kernels K Ke Ker Kern s ls els nels LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Relation relation R Re Rel Rela n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Extraction extraction E Ex Ext Extr n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 2 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Advances advances A Ad Adv Adva s es ces nces LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "in in i in in in n in in in LINEIN NOCAPS NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Neural neural N Ne Neu Neur l al ral ural LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Processing processing P Pr Pro Proc g ng ing sing LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 2 <reference-block>\n" +
                "18 18 1 18 18 18 8 18 18 18 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "MIT mit M MI MIT MIT T IT MIT MIT LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Press press P Pr Pre Pres s ss ess ress LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "3 3 3 3 3 3 3 3 3 3 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "Glickman glickman G Gl Gli Glic n an man kman LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "O o O O O O O O O O LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "2006 2006 2 20 200 2006 6 06 006 2006 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Recognising recognising R Re Rec Reco g ng ing sing LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Quiñonero quiñonero Q Qu Qui Quiñ o ro ero nero LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "- - - - - - - - - - LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 3 <reference-block>\n" +
                "Candela candela C Ca Can Cand a la ela dela LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "et et e et et et t et et et LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "al al a al al al l al al al LINEIN NOCAPS NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "editors editors e ed edi edit s rs ors tors LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "MLCW mlcw M ML MLC MLCW W CW LCW MLCW LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 3 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 3 <reference-block>\n" +
                "LNAI lnai L LN LNA LNAI I AI NAI LNAI LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 3 <reference-block>\n" +
                "Volume volume V Vo Vol Volu e me ume lume LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "3944 3944 3 39 394 3944 4 44 944 3944 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "177 177 1 17 177 177 7 77 177 177 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "190 190 1 19 190 190 0 90 190 190 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Springer springer S Sp Spr Spri r er ger nger LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "Verlag verlag V Ve Ver Verl g ag lag rlag LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "4 4 4 4 4 4 4 4 4 4 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Jenny jenny J Je Jen Jenn y ny nny enny LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Rose rose R Ro Ros Rose e se ose Rose LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Finkel finkel F Fi Fin Fink l el kel nkel LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "Trond trond T Tr Tro Tron d nd ond rond LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Grenager grenager G Gr Gre Gren r er ger ager LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 4 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Christopher christopher C Ch Chr Chri r er her pher LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Manning manning M Ma Man Mann g ng ing ning LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Incorporating incorporating I In Inc Inco g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Non non N No Non Non n on Non Non LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 4 <reference-block>\n" +
                "local local l lo loc loca l al cal ocal LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "into into i in int into o to nto into LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Information information I In Inf Info n on ion tion LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Extraction extraction E Ex Ext Extr n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "by by b by by by y by by by LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Gibbs gibbs G Gi Gib Gibb s bs bbs ibbs LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Sampling sampling S Sa Sam Samp g ng ing ling LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 4 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "43nd 43nd 4 43 43n 43nd d nd 3nd 43nd LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Annual annual A An Ann Annu l al ual nual LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "Meeting meeting M Me Mee Meet g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 4 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Association association A As Ass Asso n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "for for f fo for for r or for for LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Linguistics linguistics L Li Lin Ling s cs ics tics LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "( ( ( ( ( ( ( ( ( ( LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 OPENBRACKET 5 <reference-block>\n" +
                "ACL acl A AC ACL ACL L CL ACL ACL LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "2005 2005 2 20 200 2005 5 05 005 2005 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ") ) ) ) ) ) ) ) ) ) LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 ENDBRACKET 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "pp pp p pp pp pp p pp pp pp LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "363 363 3 36 363 363 3 63 363 363 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 5 <reference-block>\n" +
                "370 370 3 37 370 370 0 70 370 370 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "5 5 5 5 5 5 5 5 5 5 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "Giampiccolo giampiccolo G Gi Gia Giam o lo olo colo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "Magnini magnini M Ma Mag Magn i ni ini nini LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "Dagan dagan D Da Dag Daga n an gan agan LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "I i I I I I I I I I LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Dolan dolan D Do Dol Dola n an lan olan LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 5 <reference-block>\n" +
                "B b B B B B B B B B LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 5 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 5 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Third third T Th Thi Thir d rd ird hird LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "PASCAL pascal P PA PAS PASC L AL CAL SCAL LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 5 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEEND INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Challenge challenge C Ch Cha Chal e ge nge enge LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Paraphrasing paraphrasing P Pa Par Para g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "1 1 1 1 1 1 1 1 1 1 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "9 9 9 9 9 9 9 9 9 9 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "Prague prague P Pr Pra Prag e ue gue ague LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "June june J Ju Jun June e ne une June LINEIN INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 6 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "6 6 6 6 6 6 6 6 6 6 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "Gildea gildea G Gi Gil Gild a ea dea ldea LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Palmer palmer P Pa Pal Palm r er mer lmer LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 6 <reference-block>\n" +
                "M m M M M M M M M M LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 6 <reference-block>\n" +
                "The the T Th The The e he The The LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Necessity necessity N Ne Nec Nece y ty ity sity LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Parsing parsing P Pa Par Pars g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Predicate predicate P Pr Pre Pred e te ate cate LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Argument argument A Ar Arg Argu t nt ent ment LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                "Recognition recognition R Re Rec Reco n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 6 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "40th 40th 4 40 40t 40th h th 0th 40th LINESTART NOCAPS CONTAINSDIGITS 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Meeting meeting M Me Mee Meet g ng ing ting LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Association association A As Ass Asso n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Linguistics linguistics L Li Lin Ling s cs ics tics LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "( ( ( ( ( ( ( ( ( ( LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 OPENBRACKET 7 <reference-block>\n" +
                "ACL acl A AC ACL ACL L CL ACL ACL LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ") ) ) ) ) ) ) ) ) ) LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 ENDBRACKET 7 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 7 <reference-block>\n" +
                "239 239 2 23 239 239 9 39 239 239 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 7 <reference-block>\n" +
                "246 246 2 24 246 246 6 46 246 246 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "Philadelphia philadelphia P Ph Phi Phil a ia hia phia LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "PA pa P PA PA PA A PA PA PA LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "7 7 7 7 7 7 7 7 7 7 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "Lin lin L Li Lin Lin n in Lin Lin LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 7 <reference-block>\n" +
                "D d D D D D D D D D LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "1998 1998 1 19 199 1998 8 98 998 1998 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "Dependency dependency D De Dep Depe y cy ncy ency LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 7 <reference-block>\n" +
                "based based b ba bas base d ed sed ased LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Evaluation evaluation E Ev Eva Eval n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "MINIPAR minipar M MI MIN MINI R AR PAR IPAR LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 7 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "Evaluation evaluation E Ev Eva Eval n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 7 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Parsing parsing P Pa Par Pars g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Systems systems S Sy Sys Syst s ms ems tems LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "8 8 8 8 8 8 8 8 8 8 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Piskorski piskorski P Pi Pis Pisk i ki ski rski LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "J j J J J J J J J J LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "A a A A A A A A A A LINEIN ALLCAP NODIGIT 1 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Shallow shallow S Sh Sha Shal w ow low llow LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Text text T Te Tex Text t xt ext Text LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Processing processing P Pr Pro Proc g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Core core C Co Cor Core e re ore Core LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Engine engine E En Eng Engi e ne ine gine LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Journal journal J Jo Jou Jour l al nal rnal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Computational computational C Co Com Comp l al nal onal LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Intelligence intelligence I In Int Inte e ce nce ence LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "Volume volume V Vo Vol Volu e me ume lume LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "18 18 1 18 18 18 8 18 18 18 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "Number number N Nu Num Numb r er ber mber LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "3 3 3 3 3 3 3 3 3 3 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "2002 2002 2 20 200 2002 2 02 002 2002 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 8 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "451 451 4 45 451 451 1 51 451 451 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 8 <reference-block>\n" +
                "476 476 4 47 476 476 6 76 476 476 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "9 9 9 9 9 9 9 9 9 9 LINESTART NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 8 <reference-block>\n" +
                "Anselmo anselmo A An Ans Anse o mo lmo elmo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                "Peñas peñas P Pe Peñ Peña s as ñas eñas LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 8 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "Álvaro álvaro Á Ál Álv Álva o ro aro varo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Rodrigo rodrigo R Ro Rod Rodr o go igo rigo LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "Felisa felisa F Fe Fel Feli a sa isa lisa LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Verdejo verdejo V Ve Ver Verd o jo ejo dejo LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Overview overview O Ov Ove Over w ew iew view LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Answer answer A An Ans Answ r er wer swer LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Validation validation V Va Val Vali n on ion tion LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Exercise exercise E Ex Exe Exer e se ise cise LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "CLEF clef C CL CLE CLEF F EF LEF CLEF LINEEND ALLCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Working working W Wo Wor Work g ng ing king LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Notes notes N No Not Note s es tes otes LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "10 10 1 10 10 10 0 10 10 10 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 9 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "2007a 2007a 2 20 200 2007 a 7a 07a 007a LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 9 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "a a a a a a a a a a LINEIN NOCAPS NODIGIT 1 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Subsequence subsequence S Su Sub Subs e ce nce ence LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Kernel kernel K Ke Ker Kern l el nel rnel LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                "Method method M Me Met Meth d od hod thod LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 9 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Proc proc P Pr Pro Proc c oc roc Proc LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "of of o of of of f of of of LINEEND NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "AAAI aaai A AA AAA AAAI I AI AAI AAAI LINESTART ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "11 11 1 11 11 11 1 11 11 11 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "2007b 2007b 2 20 200 2007 b 7b 07b 007b LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Sentence sentence S Se Sen Sent e ce nce ence LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Similarity similarity S Si Sim Simi y ty ity rity LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "based based b ba bas base d ed sed ased LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Dependency dependency D De Dep Depe y cy ncy ency LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Tree tree T Tr Tre Tree e ee ree Tree LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Skeletons skeletons S Sk Ske Skel s ns ons tons LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 10 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Proceedings proceedings P Pr Pro Proc s gs ngs ings LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "the the t th the the e he the the LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Workshop workshop W Wo Wor Work p op hop shop LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "on on o on on on n on on on LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "Paraphrasing paraphrasing P Pa Par Para g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 10 <reference-block>\n" +
                "pages pages p pa pag page s es ges ages LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "36 36 3 36 36 36 6 36 36 36 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 10 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "41 41 4 41 41 41 1 41 41 41 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "Prague prague P Pr Pra Prag e ue gue ague LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "June june J Ju Jun June e ne une June LINEEND INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "12 12 1 12 12 12 2 12 12 12 LINESTART NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "Wang wang W Wa Wan Wang g ng ang Wang LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "R r R R R R R R R R LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "and and a an and and d nd and and LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Neumann neumann N Ne Neu Neum n nn ann mann LINEIN INITCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "G g G G G G G G G G LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "2007c 2007c 2 20 200 2007 c 7c 07c 007c LINEIN NOCAPS CONTAINSDIGITS 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "DFKI dfki D DF DFK DFKI I KI FKI DFKI LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "– – – – – – – – – – LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "LT lt L LT LT LT T LT LT LT LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "at at a at at at t at at at LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "AVE ave A AV AVE AVE E VE AVE AVE LINEIN ALLCAP NODIGIT 0 1 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 11 <reference-block>\n" +
                "Using using U Us Usi Usin g ng ing sing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Recognizing recognizing R Re Rec Reco g ng ing zing LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Textual textual T Te Tex Text l al ual tual LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Entailment entailment E En Ent Enta t nt ent ment LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "for for f fo for for r or for for LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Answer answer A An Ans Answ r er wer swer LINEEND INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Validation validation V Va Val Vali n on ion tion LINESTART INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ". . . . . . . . . . LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 11 <reference-block>\n" +
                "In in I In In In n In In In LINEIN INITCAP NODIGIT 0 1 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "online online o on onl onli e ne ine line LINEIN NOCAPS NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "proceedings proceedings p pr pro proc s gs ngs ings LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "of of o of of of f of of of LINEIN NOCAPS NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "CLEF clef C CL CLE CLEF F EF LEF CLEF LINEIN ALLCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Working working W Wo Wor Work g ng ing king LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                "Notes notes N No Not Note s es tes otes LINEIN INITCAP NODIGIT 0 0 1 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 11 <reference-block>\n" +
                "ISBN isbn I IS ISB ISBN N BN SBN ISBN LINEIN ALLCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 11 <reference-block>\n" +
                ": : : : : : : : : : LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 PUNCT 12 <reference-block>\n" +
                "2 2 2 2 2 2 2 2 2 2 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "912335 912335 9 91 912 9123 5 35 335 2335 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "31 31 3 31 31 31 1 31 31 31 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                "- - - - - - - - - - LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 HYPHEN 12 <reference-block>\n" +
                "0 0 0 0 0 0 0 0 0 0 LINEIN NOCAPS ALLDIGIT 1 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "September september S Se Sep Sept r er ber mber LINEIN INITCAP NODIGIT 0 1 0 0 0 0 1 0 0 NOPUNCT 12 <reference-block>\n" +
                "2007 2007 2 20 200 2007 7 07 007 2007 LINEIN NOCAPS ALLDIGIT 0 0 0 0 0 1 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEIN ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "Budapest budapest B Bu Bud Buda t st est pest LINEIN INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ", , , , , , , , , , LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 COMMA 12 <reference-block>\n" +
                "Hungary hungary H Hu Hun Hung y ry ary gary LINESTART INITCAP NODIGIT 0 0 0 0 0 0 0 0 0 NOPUNCT 12 <reference-block>\n" +
                ". . . . . . . . . . LINEEND ALLCAP NODIGIT 1 0 0 0 0 0 0 0 0 DOT 12 <reference-block>\n" +
                "\n";

        System.out.println(s.length());
        System.out.println(s);
    }
}
