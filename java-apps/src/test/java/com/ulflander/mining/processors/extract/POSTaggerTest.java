package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.Document;
import org.junit.Before;
import org.junit.Test;

public class POSTaggerTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenWeightConsolidation");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.AcronymExtractor");
        s.addProcessor("extract.en.EnTokenPOSConsolidation");
        s.addProcessor("extract.en.EnTokenSingularization");
        s.addProcessor("extract.TokenCorpusConsolidation");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        s.addProcessor("extract.TokenInferConsolidation");
        s.addProcessor("extract.en.EnOperatorBasedConsolidation");
        s.addProcessor("extract.TokenAggregator");
        s.addProcessor("extract.AggregatedCorpusGuesser");
        s.addProcessor("extract.EntityLookup");
        s.addProcessor("extract.EntityBasedAggregator");
        s.addProcessor("extract.EntityConsolidation");
        s.addProcessor("extract.EntityCleaner");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
        s.addProcessor("augment.QualityEvaluator");
    }

    @Test
    public void basic2Test () {
        d.setSurface("Ukraine dismisses Russian threats to intervene, continues ‘anti-terrorist’ campaign\n" +
                "\n" +
                "By William Booth and Michael Birnbaum, The Washington Post\n" +
                "Updated: Friday, April 25, 3:25 PM\n" +
                "DONETSK, Ukraine — The Ukrainian government Friday brushed off Russian threats of intervention in a tense standoff in eastern Ukraine, declaring that Ukraine’s “anti-terrorist operation” against pro-Russian militiamen occupying government buildings would continue.\n" +
                "\n" +
                "Ukrainian troops clashed with pro-Russian militants at barricades and checkpoints Thursday in eastern Ukraine, with fighting centered around the breakaway city of Slovyansk, a separatist stronghold.\n" +
                "\n" +
                "“We do not want any casualties. We will not storm the city. We realize that there may be a large number of injured people in this case,” said Vasyl Krutov, deputy chief of Ukraine’s Security Service, at a news briefing in Kiev on Friday.\n" +
                "\n" +
                "Interior Minister Arsen Avakov said Friday that Ukraine’s military move -- called ATO, for Anti-Terrorist Operation — would continue.\n" +
                "\n" +
                "“There’s been no suspension of the ATO in the face of threats from an invasion by Russian armed forces,” Avakov said.\n" +
                "\n" +
                "The Ukrainian interior minister said, “The terrorists should be on their guard around the clock. Civilians have nothing to fear.”\n" +
                "\n" +
                "German Chancellor Angela Merkel called Russian President Vladi­mir Putin on Friday to discuss the tense situation in Ukraine, Merkel’s spokesman and the Kremlin said.\n" +
                "\n" +
                "The Russian leader appeared to give no ground in his insistance that the interim government in Kiev back down against pro-Russian separatists who have seized several cities in eastern Ukraine.\n" +
                "\n" +
                "“Putin harshly condemned the attempts of the regime in Kiev to use armed forces against peaceful civilians in the southeast of the country,” the Kremlin said in a statement.\n" +
                "\n" +
                "But there were few signs of open conflict Friday in or around the contested eastern cities.\n" +
                "\n" +
                "In Mariupol, a southeastern Ukrainian port city on the Sea of Azov, control of the local government was back in the hands of the separatist People’s Republic of Donetsk on Friday, a day after Kiev proclaimed the City Hall there liberated from pro-Russian militiamen. At some point after that declaration, the local mayor and police agreed to cede the city’s administrative headquarters once again to the separatists, who were busy Friday making molotov cocktails in the basement of City Hall.\n" +
                "\n" +
                "It was not immediately clear why the local government caved, but the development appeared to illustrate the challenges that Kiev faces in trying to regain control in eastern Ukraine.\n" +
                "\n" +
                "In Moscow, Russian Foreign Minister Sergei Lavrov warned Friday that the West wants to take control of Ukraine and is obsessed by its geopolitical ambitions.\n" +
                "\n" +
                "“Without batting an eye, our Western partners keep demanding day after day that Russia stop interfering in Ukrainian affairs, pull out troops and remove certain agents who have reportedly been caught in the southeast and who are reportedly guiding these processes,” Lavrov said at a forum of young Russian diplomats.\n" +
                "\n" +
                "On Thursday night, Secretary of State John F. Kerry said that Russia “has refused to take a single step in the right direction.”\n" +
                "\n" +
                "Kerry said the U.S. government was preparing another round of economic sanctions aimed at constraining Russian ambitions in Ukraine.\n" +
                "\n" +
                "“I have told John Kerry many times — he raised the question about two weeks ago — that they should show Russian agents, if they have really been caught by the Ukrainian services, to people; they should show them on TV,” Lavrov said.\n" +
                "\n" +
                "He spoke a day after Russia began military drills on its border with Ukraine in response to Ukrainian military operations that killed “up to five” pro-Russian militants, according to Ukrainian officials.\n" +
                "\n" +
                "Russian President Vladimir Putin condemned the Ukrainian actions, and his top deputies said a Ukrainian mobilization in the restive eastern part of the country would elicit a Russian response. The tit-for-tat military movements brought the two sides closer to a direct armed confrontation in a standoff that analysts call one of the most dangerous on European soil since the end of the Cold War.\n" +
                "\n" +
                "“If the Kiev regime has started to use the army against the population inside the country, it beyond any doubt is a very serious crime,” Putin said at a media forum in St. Petersburg. He added that if Ukrainian authorities escalated the confrontation, there would be “consequences.”\n" +
                "\n" +
                "After a day of increasingly dire reports from the ground, the Obama administration struck back, at least verbally. “If Russia continues in this direction, it will not just be a grave mistake, it will be an expensive mistake,” Kerry said in Washington. Although he announced no new sanctions, Kerry said, “The window [for Russia] to change course is closing. . . . We are ready to act.”");

        s.submit(d);
        trace (d);
    }

    @Test
    public void archeoTest() {
        d.setSurface("Stanford Archaeologist Laura Jones celebrated as an innovative professional and trusted partner\n" +
                "Laura Jones, one of this year's Amy J. Blue Award winners, is director of heritage services and special projects, and university archaeologist. She has earned a reputation as a \"champion of all things Stanford\" and for setting a high ethical standard in the care of tribal remains.\n" +
                "\n" +
                "BY KATHLEEN J. SULLIVAN\n" +
                "\n" +
                "Laura Jones\n" +
                "Laura Jones, university archaeologist, was honored with an Amy J. Blue Award for her work preserving archaeological sites and historic buildings and being 'a champion of all things Stanford.\n" +
                "\n" +
                "Like children who watched the old game show Where in the World Is Carmen Sandiego?, faculty, staff and students at Stanford could play \"Where on the Farm is Laura Jones?\"\n" +
                "\n" +
                "Jones, university archaeologist, could be in her cubicle in Land, Buildings and Real Estate's headquarters on Porter Drive. Or in the Stanford Archaeology Center just off the Main Quad on Escondido Mall. Or in the Field Conservation Facility, a brown modular bedecked with succulents on Oak Road on the west side of campus.\n" +
                "\n" +
                "Or, these days, she could be traipsing around Jasper Ridge Biological Preserve with a coterie of students on a carefully planned search for the labor camp associated with the construction of Searsville Dam, which was built in 1892 by 100 men for the Spring Valley Water District.\n" +
                "\n" +
                "\"We suspect there will be a very large archaeological deposit from the labor camp,\" Jones said. \"It's somewhere inside Jasper Ridge. We have to be careful while we're looking for it, because there are poison oak plants, ticks and rattlesnakes. Archaeology is a field science and there are hazards. It's a good thing to teach students. I'm the only one that's been bitten by a tick yet this year.\"\n" +
                "\n" +
                "Jones is teaching the four undergraduates and three graduate students enrolled in Archaeological Field Survey Methods how to lay out 10-meter transect grids with tape and walk the grid while closely scrutinizing the ground.\n" +
                "\n" +
                "\"We don't dig,\" she said. \"We go out and look for sites out there in the hinterlands. We know where the 65 prehistoric Native American sites are on Stanford lands. But we've been working to catch up on historic archaeological sites, like the house of Henry P. Coon, who served as mayor of San Francisco during the American Civil War.\"\n" +
                "\n" +
                "An earlier class found the Coon house, which was built at a site near today's Interstate 280 and collapsed during the 1906 earthquake. Another class found the house of Denis Martin, a California pioneer whose land holdings included the Jasper Ridge Biological Preserve.\n" +
                "\n" +
                "\"It's enormous fun,\" Jones said. \"I can't tell you how much fun it is. We spent three hours on Monday afternoon tramping around in our little grids. I get a huge amount of energy from doing that with students.\"\n" +
                "\n" +
                "Jones is one of this year's recipients of the Amy J. Blue Award.\n" +
                "\n" +
                "A rural childhood\n" +
                "\n" +
                "Jones spent part of her childhood in South Carolina, and a lot of time on the farm where her grandparents grew cotton and tobacco when she was little, and grew corn, wheat and soybeans, and raised hogs when she was older. When Jones was in grade school, her family moved to Iowa City, where her father taught education at the University of Iowa.\n" +
                "\n" +
                "\"Growing up in rural states, I was certainly aware of dirt – of soil,\" she said. \"I had a foot in academia and in the outdoors. I enjoyed the seasons and working with my hands. My grandmother canned, tatted, knitted and quilted, so I had the sense there was a kind of dignity in that work. My family also was very interested in South Carolina history.\"\n" +
                "\n" +
                "After earning a bachelor's degree in anthropology at the University of California, San Diego, Jones headed north to Stanford, where she earned a master's degree (1985) and a doctorate (1991) in material culture and applied anthropology studies.\n" +
                "\n" +
                "Soon, she was working for the Ohlone Tribe, which was excavating hundreds of bodies of tribal members discovered near construction sites in San Jose and Milpitas to be reburied.\n" +
                "\n" +
                "\"It was really an important moment in Native American cemetery protection, because the tribe insisted that they be in charge of handling the human remains and caring for the graves,\" Jones said. \"I was their employee. Traditionally, it had been the other way around. Archaeologists would hire Native Americans to observe cemetery work. So I trained Native Americans in excavation techniques. Descendents of the Ohlone Tribe excavated all of the bodies. I was there to support their work – I wrote reports and attended meetings – and served as the liaison with the landowners.\"\n" +
                "\n" +
                "In 1994, when Stanford was looking for a campus archaeologist, Jones didn't bother applying for the job, because she'd been rejected for the same post three years earlier for being \"unsupervisable.\" This time, Stanford called her, and hired her part-time under a six-month probation to work on the Sand Hill Road expansion project.\n" +
                "\n" +
                "\"At the end of six months, they decided I was agreeable enough to stay, and they offered to make me a full-time campus archaeologist if I would take on historic preservation as well,\" Jones said. \"So in 1995, I took charge of the Loma Prieta Earthquake Recovery Projects funded by the Federal Emergency Management Agency.\"\n" +
                "\n" +
                "She handled the negotiations with county, state and federal officials as Stanford set about repairing, restoring or renovating buildings damaged in the 1989 earthquake, including the Stanford Museum, which reopened in 1999 as the Cantor Arts Center; Green Library; Hanna House, a Frank Lloyd Wright masterpiece; and several buildings on the Main Quad.\n" +
                "\n" +
                "Preserving archaeological sites, historic buildings and Stanford's leadership as a world-class university\n" +
                "\n" +
                "In a joint letter nominating Jones for an Amy J. Blue Award, the program staff of the Native American Cultural Center (Karen Biestman, associate dean and director; Denni Woodward, assistant director; and Greg Graves, graduate recruitment and retention coordinator), wrote that Jones has left a \"deep and widespread footprint\" at Stanford that began when she was in graduate school.\n" +
                "\n" +
                "A year before the passage of the Native American Grave Protection and Repatriation Act of 1990, leaders from the Muwekma Ohlone Tribe appealed to the university to repatriate 550 skeletal remains of their ancestors. Jones, working with Stanford's archaeologist, was central to those discussions. When Stanford decided to repatriate the remains, Jones personally delivered the Ohlone ancestors to the Coyote Hills in Fremont for reburial.\n" +
                "\n" +
                "\"This event was groundbreaking on many fronts,\" they wrote. \"In fact, it earned her much criticism from museum professionals and archaeology peers across the country that feared Stanford's action would signal the removal of osteological research materials from teaching collection shelves. In the end, not only were their concerns unfounded, but also it became apparent it was the right thing to do. The decision earned Stanford a reputation for setting a high ethical standard in the discipline, and earned Laura a reputation as an innovative professional, and trusted tribal and Native Community partner – all of which persist today.\"\n" +
                "\n" +
                "Describing Jones as \"a champion of all things Stanford,\" they cited her work overseeing the excavation of the Men's Gymnasium, the repair of the Angel of Grief historic memorial and the restoration of the Arizona Cactus Garden near the Stanford Family Mausoleum.\n" +
                "\n" +
                "Currently, Jones reviews historic preservation issues for faculty/staff housing and leased properties, including the Research Park, Webb Ranch and the Palo Alto Train Depot. Her job is to identify Stanford's historical assets and analyze their historic importance.\n" +
                "\n" +
                "\"Stanford has a value, which is we should preserve archaeological sites and historic buildings,\" she said. \"But we also have a goal, which is preserving our leadership as a world-class university. To achieve that goal we want to build a new hospital, to build a new chemistry building or to be better stewards of the creek. It's my role to see that we respect important archaeological and historic sites and that our stewardship of those sites is rewarded when we get permits for our construction projects. I work in Stanford's Land, Buildings and Real Estate organization, and we're really in the business of getting permission for the university to do things. I work on getting that permission.\"\n" +
                "\n" +
                "For instance, Jones said, Stanford had to ask for – and won – permission from Santa Clara County to gut the Old Chemistry Building and transform it into a center devoted to undergraduate science education. The design will incorporate some of the building's key historic features, including its cast-iron stairs, wood wainscot and trim. The rest of the building will be transformed, with a library, auditorium, classrooms and laboratories.\n" +
                "\n" +
                "\"Part of my role is to show that we've thought about the historic issues related to the Old Chemistry Building and have presented a thoughtful response about remodeling it,\" she said. \"Our preservation approach to Old Chem is that you should know you're in a building that was built in 1903 and that was remodeled in 2014 – a place where you will be able to see both the old and the modern at the same time.\"\n" +
                "\n" +
                "Jones said her favorite excavation was the Stanford Family Mansion, which collapsed during the 1906 earthquake, was demolished to the basement and covered with a lawn.\n" +
                "\n" +
                "\"Every spring for about six years, I would take a class out there, and we would dig up the basement,\" she said.\n" +
                "\n" +
                "One of the treasures they unearthed was Jane Stanford's ceramic pink and white toilet set – a pitcher, bowl, sponge tray and footbath. All told, the family mementos filled 75 boxes.\n" +
                "\n" +
                "\"It was huuugely fun, because it connected students to Stanford's history, and because they found treasure every day,\" Jones said. \"In archaeology, there is usually a lot of sifting of dirt and then a few tiny clues. But the mansion's basement was full of treasures. You couldn't plan it better as an archaeology class.\"\n" +
                "\n" +
                "An awards ceremony will be held on Wednesday, May 21, at 3:30 p.m. in Lagunita Courtyard to honor Jones and this year's other Amy J. Blue Award winners – Laurette Beeson, assistant dean in the Graduate Life Office, and Mark Urbanek, technical manager in the Film & Media Studies Program.  President John Hennessy will present the awards. The ceremony is open to friends, family members and colleagues of the recipients. ");

        s.submit(d);
        trace (d);
    }

    @Test
    public void basicTest () {
        d.setSurface("After complaining that he lacked time to mount a full defense of his global foreign policy approach, the president took more than seven minutes to address the criticism. He didn't name any particular critics but may have had in mind both former Bush administration officials and think tank experts, as well as some more hawkish voices on Capitol Hill, such as Sen. John McCain (R-Ariz..) (A spokesman for McCain did not respond to a request for comment.) \n\n Pierre E. Gentil, 61 years old, will join the board of Vinci as a "
                + "non-executive director in the USA office on Nov. 29, his Twitter id is @pierre_vinken and "
                + "he was born in Paris, France in 1954. Paris Hilton will give a speech for a salary of $10 millions at the China Construction Bank to the board of Vinci for that purpose. "
                + "The city has one of the largest GDPs in the world, €607 billion (US$845 billion) as of 2011. MOSCOW — President Vladimir V Putin of Russia emphasized on Thursday that the upper chamber of the Russian Parliament had authorized him to use military force if necessary in eastern Ukraine, and also stressed Russia’s historical claim to the territory, repeatedly referring to it as “new Russia” and saying that only “God knows” why it became part of Ukraine. By ANDREW E. KRAMER\n" +
                "The firefight in the city of Mariupol, the most lethal so far in eastern Ukraine, underscored the worsening security situation a day after government forces were humiliated by militants. " +
                "There are several Ukrainian units on the peninsula, including three anti-aircraft regiments, the 204th Tactical Aviation Brigade and Ukraine’s navy headquarters. These forces are no match for Russian troops." +
                " According to Reuters, Ukraine’s military is on “full combat alert.”");

        s.submit(d);
        trace (d);
    }

    @Test
    public void entitiesTest () {
        Document d = new Document();
        d.setSurface("U.S. rebukes Iran’s U.N. envoy pick over 1979 embassy attack\n\nStoned mom avoids jail after driving 12 miles with baby on roof\n\nMore than 100 ‘inappropriate’ encounters between NYC school staffers, students since 2009: report\n\nJoe Biden to Boston bombing survivors: ‘America will never, ever stand down’\n\nFBI failed to throughly vet Boston bombing suspect after Russian lead, report finds");
        s.submit(d);
        trace(d);
    }


    @Test
    public void entities2Test () {
        Document d = new Document();
        d.setSurface("Cuba: On 6 May the Cuban press published a statement by the interior ministry announcing the arrest on 26 April of four Cuban exiles, resident in the US, accused of planning terrorist activity in Cuba. The four men, who the interior ministry links to Luis Posada Carriles, a Cuban exile and former US Central Intelligence Agency (CIA) operative living in Miami, Florida, were named as José Ortega Amador, Obdulio Rodríguez González, Raibel Pacheco Santos and Félix Monzón Álvarez. According to the statement, three of them had been traveling to and from Cuba since mid-2013 to plan an attack on military installations – which the Miami Herald notes is the first such violent plot reported in over a decade. According to the statement, the Cuban government said that it would contact US officials about the investigation. The US State Department and the Federal Bureau of Investigation in Miami have made no comment yet on the Cuban announcement.");
        s.submit(d);
        trace(d);
    }





}
