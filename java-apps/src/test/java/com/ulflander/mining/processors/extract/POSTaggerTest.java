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
        s.addProcessor("extract.AggregatedTypeInferenceToEntity");
        s.addProcessor("extract.UnknownPersonToEntity");
        s.addProcessor("extract.TypeInferenceToEntity");
        s.addProcessor("extract.EntityConsolidation");
        s.addProcessor("extract.EntityCleaner");
        s.addProcessor("extract.EntitySelector");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
        s.addProcessor("augment.QualityEvaluator");
    }

    @Test
    public void again() {
        d.setSurface("“I am big. It’s the pictures that got small.”\n" +
                "\n" +
                "Now, to state the obvious: Angelina Jolie is about as distant from a washed-up Norma Desmond as it is possible to be on the celebrity-relevance spectrum. And her new vehicle, Maleficent, is not small in the traditional sense, but rather in the increasingly common contemporary sense: yet another in a string of gazillion-dollar special-effects extravaganzas grafted onto flimsy, nonsensical scripts and featuring an array of two-dimensional performances.\n" +
                "\n" +
                "Related Story\n" +
                "\n" +
                "The Cyclical Darkness of Fairy Tales\n" +
                "Still, the old line from Sunset Boulevard can’t help but resonate. Jolie is, after all, big in the way that silent film stars were big before the talkies swept them away. And as one of a tiny number of female stars who can open a major movie herself, she all too often finds her charisma swallowed up by the hubbub of blockbuster filmmaking: the stunts, the effects, the desperate pursuit of scale and velocity.\n" +
                "\n" +
                "What was Jolie’s last genuinely memorable role? The Tourist? Salt? Changeling? She sauntered through her effortless brand of sexual cool in Mr. & Mrs. Smith as well as in supporting roles in Wanted, Beowulf, and Sky Captain and the World of Tomorrow. And of course she voices a tiger in the Kung Fu Panda movies. For an indisputably A-list actress, it adds up to an awful lot of B-list roles. Indeed, unlike fellow cinematic icons such as her quasi-spouse Brad Pitt and his pal George Clooney, Jolie seems almost to have transcended her film career altogether. Acting often looks as though it's an occasional sideline to her day job, the no doubt taxing business of being Angelina Jolie.\n" +
                "\n" +
                "Maleficent was intended to remedy this, to reestablish Jolie center-stage, playing a villainess as iconic as herself. Alas, Disney’s subversive retelling of its own 1959 animated classic Sleeping Beauty is an utter mess. At once overblown and under-baked, the movie is a morally and tonally confused collection of sequences that never cohere into a compelling story.\n" +
                "\n" +
                "Robert Stromberg makes his directorial debut with the film, and it will surprise precisely no one who sees it that he comes from the world of special effects. (Stromberg won art direction Oscars for Avatar and Alice in Wonderland, and worked on last year’s Oz the Great and Powerful, among others.) From its opening frames, Maleficent is crammed with CGI wonders: Ent-like warrior-trees, mushroomy gnomes, acre after acre of blossoming Technicolor jungle. There are beautiful elements here and there, but they are piled so thick upon one another that they quickly become visually exhausting. Moreover, a sameness seems to be creeping into even the most accomplished CGI artistry: Do I recognize that particular magical forest creature from the Lord of the Rings? Narnia? Snow White and the Huntsman? I’m not certain, but I know I've seen it somewhere before.\n" +
                "\n" +
                "The tale that unfolds in front of this backdrop is an inversion of Charles Perrault’s classic folk tale, told from the perspective of its antagonist, a la Wicked or John Gardner’s novel Grendel. It begins with a young Maleficent, already the most powerful of the fairies—her magnificent wings are a sight to behold, half-bat, half-eagle—becoming friends with a human boy, Stefan. Their fates are complicated, however, when the human king declares war on the land of the fairies—cue the requisite effects-laden battle between armored soldiers and fantastical woodland beasts—and then promises his throne to any man who will destroy Maleficent (now played, in adulthood, by Jolie).\n" +
                "\n" +
                "Stefan has always been an ambitious fellow. (We know this because we’ve been told so in voiceover—more on this later.) So the young man, now played by South African actor Sharlto Copley, visits his old friend Maleficent to reminisce over old times and then dose her with a potion that renders her unconscious (yes: a fairy-tale roofie). When the moment comes to kill her, however, he relents, instead severing her beautiful wings and taking them with him as an offering to the king. Maleficent awakes screaming: betrayed, defiled, mutilated, her most wondrous gift torn from her. (Have I mentioned yet that this is not really a kids’ film?)\n" +
                "\n" +
                "From here, the story unfolds along more familiar lines: Years later, Stefan, now the king, and his wife have a beautiful baby daughter, Aurora; at her christening, Maleficent shows up, wreathed in green flame, to curse the child that on her 16th birthday she will prick her finger on the spindle of a spinning wheel and fall into a deathlike sleep from which she can be awakened only by the kiss of true love; in fear for her safety, the king sends Aurora away to be raised incognito by three dim but gentle fairies (Imelda Staunton, Juno Temple, and Lesley Manville as, respectively, Flora Knotgrass, Fauna Thistlewit, and Merryweather Flittle).\n" +
                "\n" +
                "This middle section of the film abandons its dark early vision almost entirely, instead offering heavy doses of feeble fairy slapstick. Maleficent mellows, too, playing practical jokes on her fairy cousins and gradually warming to Aurora as she grows into girlhood (and is now played by Elle Fanning). Unlike Disney’s earlier telling, in this version, far from losing track of Aurora, Maleficent watches over her as a kind of Bizarro fairy godmother. But as the girl reaches her 16th birthday, darkness again encroaches….\n" +
                "\n" +
                "The plot is frequently nudged forward by long bouts of expository narration, suggesting that considerable stretches of footage may have wound up on the cutting room floor.\n" +
                "There were reports of difficulties during Maleficent’s production (tension between Jolie and Stromberg; late reshoots that brought in John Lee Hancock, director of The Blind Side and Saving Mr. Banks), and the final product feels like a grab bag of disjointed ideas awkwardly stitched together. The rape/revenge theme that sets the plot in motion is exceedingly grim, and coexists uneasily with the stabs at comedy that punctuate the middle third. The result is neither a brooding, arty, grown-up fairy tale (like, say, Neil Jordan’s The Company of Wolves) nor a particularly kid-friendly one, but instead an uncomfortable commingling of the two. By would-be blockbuster standards, the film is rather short (just 96 minutes) and its plot is frequently nudged forward by long bouts of expository narration, suggesting that considerable stretches of footage may have wound up on the cutting room floor. Perhaps most peculiar of all, a central twist of the film—I won’t say what it is, but you’ll recognize it if you see it—is virtually identical to that of another, better Disney film that was released only last year.\n" +
                "\n" +
                "Jolie does what she can in the title role, flashing wicked grins and sporting cheekbones so sharp and jutting that one could juice oranges on them. But the script (by Linda Woolverton) complicates Maleficent without deepening her. As a cameo, Jolie’s performance might have proven spectacular; but as a protagonist, she doesn’t give us enough to hold onto. For all her efforts, she remains yet another special effect, however spectacular.\n" +
                "\n" +
                "The rest of the cast fares far worse. Fanning is pretty but forgettable as Aurora, and Copley’s Stefan is even more testily over-the-top than the assassin played by the actor in last summer’s Elysium. And the less said of Staunton, Temple, and Manville’s bumbling, CGI-ified fairies, the happier all three actresses will no doubt be. Of the supporting players, only Sam Riley (Control), who plays the humanoid version of Maleficent’s raven familiar, Diaval, comes out of the film looking better than he did going in.\n" +
                "\n" +
                "So what’s next for Jolie? Kung Fu Panda 3 is due out next year. And Salt 2 (c'mon, guys, at least have the ironic sense to title it SALT II) is scheduled to follow sometime on its heels. Jolie will always be big. But, if anything, her pictures seem to be getting ever smaller.");
        s.submit(d);
        trace(d);
    }

    @Test
    public void anotherTest() {
        d.setSurface("Halep to Face Stephens in French Open’s Fourth Round\n" +
                "\n" +
                "Simona Halep reached the fourth round at a third consecutive Grand Slam tournament, beating 55th-ranked Maria-Teresa Torro-Flor of Spain at the French Open on Saturday.");

        s.submit(d);
        trace(d);
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


    @Test
    public void evenMoarTest() {
        Document d = new Document();
        d.setSurface("MAY 6, 2014\n" +
                "\n" +
                "“We are here fighting, and they are sitting around stuffing themselves,” Mr. Ponomaryov said by telephone from the city, which has been surrounded by Ukrainian military checkpoints and is in a region where shelling and shootouts have occurred in recent weeks. “It’s not a difference of opinion,” he said. “We have fundamentally opposing views.”\n" +
                "\n" +
                "Ukraine Crisis in Maps\n" +
                "\n" +
                "Mr. Ponomaryov’s statements suggested open hostility between Donetsk, where the Donetsk People’s Republic declared autonomy from Kiev after organizing a referendum this month, and several heavily armed stronghold cities to the northwest.\n" +
                "\n" +
                "But with armed groups in Slovyansk and elsewhere under the control of a shadowy military commander named Igor Strelkov, it was not immediately clear that Mr. Ponomaryov’s denunciation of his fellow revolutionaries carried any real weight.\n" +
                "\n" +
                "The separatist groups were knocked off balance earlier this month when President Vladimir V. Putin of Russia announced his intention to withdraw tens of thousands of Russian troops from the Ukrainian border, recommended that Ukraine solve its political problems through dialogue, and even gave tacit backing to the presidential election. Whether they can maintain their previous momentum without the implicit military and political backing of the Kremlin remains to be seen.\n" +
                "\n" +
                "Mr. Ponomaryov’s statements came as Ukraine’s provisional government has tried to cobble together support and improve the chances of a legitimate vote on Sunday. At a round-table session in the southern city of Mykolaiv, representatives of the Kiev government received a generally warm reception amid what seemed to be broad support for the talks, which are intended to help resolve the political crisis.\n" +
                "\n" +
                "Mykola P. Romanchuk, the governor of the Mykolaiv region, praised the effort at dialogue. “The right way to solve any problems — economic, judicial and, today, political,” Mr. Romanchuk said, “we need to solve at the round table.”\n" +
                "\n" +
                "The atmosphere was notably lighter than at the previous session in the eastern city of Kharkiv, where officials from the embattled region at times openly questioned the motives and even the legitimacy of the provisional government.\n" +
                "\n" +
                "Officials described Sunday’s presidential election as critical to Ukraine’s moving forward after the recent months of unrest. The acting prime minister, Arseniy P. Yatsenyuk, said the government was working steadily to carry out a series of structural changes, including constitutional amendments, and a decentralization plan that will give greater budget authority to local officials.\n" +
                "\n" +
                "Continue reading the main story\n" +
                "\n" +
                "Advertisement\n" +
                "\n" +
                "Mr. Yatsenyuk said the government was also committed to tightening ties with Europe through formal political and trade agreements. “We have no other way,” he said. Mr. Yatsenyuk also sounded optimistic about the east, collectively known as Donbass, where he said separatist groups were clearly weakened.\n" +
                "\n" +
                "“In the last few days, the situation in Donbass has turned around,” Mr. Yatsenyuk said. “The self-proclaimed Donbass republics will control neither Donbass nor Ukraine. I can call it an achievement of our common efforts.”\n" +
                "\n" +
                "In a visit to a military base located near the city of Slovyansk, Oleksandr V. Turchynov, Ukraine’s acting president, told local news media that Kiev’s military campaign against the rebel groups was entering its “final phase” and vowed to “cleanse the Donetsk and Lugansk regions of terrorists.”\n" +
                "\n" +
                "Denis Pushilin, the speaker for the revolutionary republic’s parliament based in Donetsk, denounced the military campaign and said that if it continued it could provoke a military response from Russia.\n" +
                "\n" +
                "Speaking in his office on the 10th floor of the seized regional administration building in Donetsk, Mr. Pushilin on Wednesday denied any rift between himself and Mr. Ponomaryov.\n" +
                "\n" +
                "He said that Mr. Ponomaryov had received “incorrect information” because the military blockade of the city had hampered communications, which could be monitored by the Ukrainian military.\n" +
                "\n" +
                "But Mr. Pushilin said that relations remained strong with Mr. Strelkov, who he said controlled all the paramilitary groups in the region.\n" +
                "\n" +
                "Ukrainian media outlets, many of them openly hostile to anti-Kiev forces in the east, have reported that heightened tensions have led to arguments and even armed clashes among rebel commanders in several cities.\n" +
                "\n" +
                "Both Mr. Pushilin and Mr. Ponomaryov denied that serious fighting had taken place.\n" +
                "\n" +
                "On Tuesday, several military trucks with armed men bearing a Russian flag sped into Donetsk from the north and surrounded the headquarters of the Ukrainian Security Service, which has been occupied by members of a Donetsk-based militia.\n" +
                "\n" +
                "Sentries dressed in military fatigues and armed with automatic rifles were posted at crossroads leading to the building, and city police officers prevented pedestrians from entering the area.\n" +
                "\n" +
                "A guard, who did not give his name but identified himself as a second lieutenant in the army of the Donetsk People’s Republic, said they had come from the city of Horlivka, which is located to the north of Donetsk, in order to “instill order” among members of a local militia.\n" +
                "\n" +
                "“There is no discipline” among them, he said, adding that the local militias were “unprofessional” and had not been registered. “Horlivka made the revolution.”\n" +
                "\n" +
                "Andrew Roth reported from Donetsk, and David M. Herszenhorn from Kiev, Ukraine.\n" +
                "\n" +
                "A version of this article appears in print on May 22, 2014, on page A12 of the New York edition with the headline: Solidarity Eludes Ukraine Separatist Groups as Presidential Election Nears. Order Reprints. Today\\'s Paper. Subscribe\n" +
                "\n" +
                "Next in Europe',\n" +
                " ");
        s.submit(d);
        trace(d);
    }



}
