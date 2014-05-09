package com.ulflander.mining.processors.extract;

import com.ulflander.AbstractTest;
import com.ulflander.app.model.Document;
import org.junit.Before;
import org.junit.Test;

public class POSTaggerTest extends AbstractTest {

    @Before
    public void setupProcessors () {
        s.addProcessor("extract.DocumentCleaner");
        s.addProcessor("extract.en.EnCommonAcronymsCleaner");
        s.addProcessor("extract.DocumentSplitter");
        s.addProcessor("extract.LanguageDetector");
        s.addProcessor("extract.POSTagger");
        s.addProcessor("extract.DocumentTokenizer");
        s.addProcessor("extract.TokenWeightConsolidation");
        s.addProcessor("extract.TokenCounter");
        s.addProcessor("extract.TokenCleaner");
        s.addProcessor("extract.AcronymExtractor");
        s.addProcessor("extract.TokenCorpusGuesser");
        s.addProcessor("extract.TokenRegExpGuesser");
        s.addProcessor("extract.en.EnTokenPOSConsolidation");
        s.addProcessor("extract.en.EnTokenSingularization");
        s.addProcessor("extract.TokenCorpusConsolidation");
        s.addProcessor("extract.TokenSiblingsConsolidation");
        s.addProcessor("extract.TokenInferConsolidation");
        s.addProcessor("extract.en.EnOperatorBasedConsolidation");
        s.addProcessor("extract.TokenAggregator");
        s.addProcessor("extract.TokenFrequency");
        s.addProcessor("extract.KeywordSelector");
        s.addProcessor("augment.BasicTextStat");
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
