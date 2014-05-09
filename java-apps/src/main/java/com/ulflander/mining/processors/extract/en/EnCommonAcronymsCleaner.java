package com.ulflander.mining.processors.extract.en;

import com.ulflander.app.model.Document;
import com.ulflander.mining.processors.Processor;
import com.ulflander.mining.processors.ProcessorDepthControl;
import com.ulflander.mining.processors.Requires;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Replace common english acronyms related to international news by their
 * entire version.
 *
 * Created by Ulflander on 4/16/14.
 */
@Requires(processors = {
        "extract.DocumentCleaner"
})
public class EnCommonAcronymsCleaner extends Processor {

    /**
     * List of acronyms.
     */
    private HashMap<String, String> acronyms;

    @Override
    public final void init() {
        this.setDepthControl(ProcessorDepthControl.DOCUMENT);

        acronyms = new HashMap<String, String>();
        acronyms.put("U\\.A\\.E\\.([^a-zA-Z])", "United Arab Emirates$1");
        acronyms.put("UAE([^a-zA-Z])", "United Arab Emirates$1");
        acronyms.put("USA([^a-zA-Z])", "United States$1");
        acronyms.put("U\\.S[.-]", "United States ");
        acronyms.put("U\\.S\\.A\\.", "United States ");
        acronyms.put("U\\.N[.-]", "United Nations ");
        acronyms.put("NY([^a-zA-Z])", "New York$1");
        acronyms.put("NYC([^a-zA-Z])", "New York$1");
        acronyms.put("NZ([^a-zA-Z])", "New Zealand$1");
        acronyms.put("U\\.n[.-]", "United Nations ");
        acronyms.put("N\\.Y[.-]", "New York ");
        acronyms.put("L\\.A[.-]", "Los Angeles ");
        acronyms.put("N\\.Z[.-]", "New Zealand ");
        acronyms.put("Int’l", "International");
        acronyms.put("Int'l", "International");
        acronyms.put("Sana a", "Sanaa");
        acronyms.put("Sana’a", "Sanaa");
        acronyms.put("Sana'a", "Sanaa");
        acronyms.put("Int’([^a-zA-Z])", "International$1");
        acronyms.put("S\\. Korea", "South Korea");
        acronyms.put("N\\. Korea", "North Korea");
        acronyms.put("S\\. Africa", "South Africa");
        acronyms.put("S Korea", "South Korea");
        acronyms.put("N Korea", "North Korea");
        acronyms.put("S Africa", "South Africa");
        acronyms.put("AU([^a-zA-Z])", "African Union$1");
        acronyms.put("A\\.U[.-]", "African Union ");
        acronyms.put("S Lanka", "Sri Lanka");
        acronyms.put("S\\. Lanka", "Sri-Lanka");
        acronyms.put("S Sudan", "South Sudan");
        acronyms.put("S\\. Sudan", "South Sudan");

        this.setInitialized(true);
    }

    @Override
    public final String describe() {
        return "Replace common english acronyms";
    }

    @Override
    public final void extractDocument(final Document doc) {
        String raw = " " + doc.getSurface() + " ";
        Iterator<String> i = acronyms.keySet().iterator();
        while (i.hasNext()) {
            String acronym = i.next();
            raw = raw.replaceAll(acronym, acronyms.get(acronym));
        }
        doc.setSurface(raw.trim());
    }
}
