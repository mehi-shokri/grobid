package org.grobid.core.data;

import org.grobid.core.main.LibraryLoader;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.grobid.core.engines.config.GrobidAnalysisConfig;
import org.grobid.core.utilities.GrobidProperties;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;


public class BiblioItemTest {

    public static final Logger LOGGER = LoggerFactory.getLogger(BiblioItemTest.class);


    @Before
    public void setUp() throws Exception {
        LibraryLoader.load();
    }
    
    private GrobidAnalysisConfig.GrobidAnalysisConfigBuilder configBuilder = (
        new GrobidAnalysisConfig.GrobidAnalysisConfigBuilder()
    );

    @BeforeClass
    public static void init() {
        GrobidProperties.getInstance();
    }

    private static Document parseXml(String xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    private static List<String> getXpathStrings(
        Document doc, String xpath_expr
    ) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(xpath_expr);
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        ArrayList<String> matchingStrings = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            matchingStrings.add(nodes.item(i).getNodeValue()); 
        }
        return matchingStrings;
    }

    @Test
    public void shouldGnerateRawAffiliationTextIfEnabled() throws Exception {
        GrobidAnalysisConfig config = configBuilder.includeRawAffiliations(true).build();
        Affiliation aff = new Affiliation();
        aff.setRawAffiliationString("raw affiliation 1");
        aff.setFailAffiliation(false);
        Person author = new Person();
        author.setLastName("Smith");
        author.setAffiliations(Arrays.asList(aff));
        BiblioItem biblioItem = new BiblioItem();
        biblioItem.setFullAuthors(Arrays.asList(author));
        biblioItem.setFullAffiliations(Arrays.asList(aff));
        String tei = biblioItem.toTEI(0, 2, config);
        LOGGER.debug("tei: {}", tei);
        Document doc = parseXml(tei);
        assertThat(
            "raw_affiliation",
            getXpathStrings(doc, "//note[@type=\"raw_affiliation\"]/text()"),
            is(Arrays.asList("raw affiliation 1"))
        );
    }

    @Test
    public void shouldIncludeMarkerInRawAffiliationText() throws Exception {
        GrobidAnalysisConfig config = configBuilder.includeRawAffiliations(true).build();
        Affiliation aff = new Affiliation();
        aff.setMarker("A");
        aff.setRawAffiliationString("raw affiliation 1");
        aff.setFailAffiliation(false);
        Person author = new Person();
        author.setLastName("Smith");
        author.setAffiliations(Arrays.asList(aff));
        BiblioItem biblioItem = new BiblioItem();
        biblioItem.setFullAuthors(Arrays.asList(author));
        biblioItem.setFullAffiliations(Arrays.asList(aff));
        String tei = biblioItem.toTEI(0, 2, config);
        LOGGER.debug("tei: {}", tei);
        Document doc = parseXml(tei);
        assertThat(
            "raw_affiliation label",
            getXpathStrings(doc, "//note[@type=\"raw_affiliation\"]/label/text()"),
            is(Arrays.asList("A"))
        );
        assertThat(
            "raw_affiliation",
            getXpathStrings(doc, "//note[@type=\"raw_affiliation\"]/text()"),
            is(Arrays.asList(" raw affiliation 1"))
        );
    }

    @Test
    public void shouldGnerateRawAffiliationTextForFailAffiliationsIfEnabled() throws Exception {
        GrobidAnalysisConfig config = configBuilder.includeRawAffiliations(true).build();
        Affiliation aff = new Affiliation();
        aff.setRawAffiliationString("raw affiliation 1");
        aff.setFailAffiliation(true);
        BiblioItem biblioItem = new BiblioItem();
        biblioItem.setFullAffiliations(Arrays.asList(aff));
        String tei = biblioItem.toTEI(0, 2, config);
        LOGGER.debug("tei: {}", tei);
        Document doc = parseXml(tei);
        assertThat(
            "raw_affiliation",
            getXpathStrings(doc, "//note[@type=\"raw_affiliation\"]/text()"),
            is(Arrays.asList("raw affiliation 1"))
        );
    }

    @Test
    public void shouldNotGnerateRawAffiliationTextIfNotEnabled() throws Exception {
        GrobidAnalysisConfig config = configBuilder.includeRawAffiliations(false).build();
        Affiliation aff = new Affiliation();
        aff.setRawAffiliationString("raw affiliation 1");
        Person author = new Person();
        author.setLastName("Smith");
        author.setAffiliations(Arrays.asList(aff));
        BiblioItem biblioItem = new BiblioItem();
        biblioItem.setFullAuthors(Arrays.asList(author));
        biblioItem.setFullAffiliations(Arrays.asList(aff));
        String tei = biblioItem.toTEI(0, 2, config);
        LOGGER.debug("tei: {}", tei);
        Document doc = parseXml(tei);
        assertThat(
            "raw_affiliation",
            getXpathStrings(doc, "//note[@type=\"raw_affiliation\"]/text()"),
            is(empty())
        );
    }

    @Test
    public void injectDOI() {
    }

    @Test
    public void correct_empty_shouldNotFail() {
        BiblioItem.correct(new BiblioItem(), new BiblioItem());
    }


    @Test
    public void correct_1author_shouldWork() {
        BiblioItem biblio1 = new BiblioItem();
        biblio1.setFullAuthors(Arrays.asList(createPerson("John", "Doe")));

        BiblioItem biblio2 = new BiblioItem();
        biblio2.setFullAuthors(Arrays.asList(createPerson("John1", "Doe")));

        BiblioItem.correct(biblio1, biblio2);

        assertThat(biblio1.getFirstAuthorSurname(), is(biblio2.getFirstAuthorSurname()));
        assertThat(biblio1.getFullAuthors().get(0).getFirstName(), is(biblio2.getFullAuthors().get(0).getFirstName()));

    }

    @Test
    public void correct_2authors_shouldMatchFullName_sholdUpdateAffiliation() {
        BiblioItem biblio1 = new BiblioItem();
        biblio1.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe"),
            createPerson("Jane", "Will")
        ));

        BiblioItem biblio2 = new BiblioItem();
        biblio2.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe", "UCLA"),
            createPerson("Jane", "Will","Harward")
        ));

        BiblioItem.correct(biblio1, biblio2);

        assertThat(biblio1.getFirstAuthorSurname(), is(biblio2.getFirstAuthorSurname()));
        assertThat(biblio1.getFullAuthors(), hasSize(2));
        assertThat(biblio1.getFullAuthors().get(0).getFirstName(), is(biblio2.getFullAuthors().get(0).getFirstName()));
        assertThat(biblio2.getFullAuthors().get(0).getAffiliations().get(0).getAffiliationString(), is("UCLA"));
        assertThat(biblio1.getFullAuthors().get(1).getFirstName(), is(biblio2.getFullAuthors().get(1).getFirstName()));
        assertThat(biblio2.getFullAuthors().get(1).getAffiliations().get(0).getAffiliationString(), is("Harward"));
    }

    @Test
    public void correct_2authors_shouldMatchFullName_sholdKeepAffiliation() {
        BiblioItem biblio1 = new BiblioItem();
        biblio1.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe", "Stanford"),
            createPerson("Jane", "Will", "Cambridge")
        ));

        BiblioItem biblio2 = new BiblioItem();
        biblio2.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe" ),
            createPerson("Jane", "Will")
        ));

        BiblioItem.correct(biblio1, biblio2);

        assertThat(biblio1.getFirstAuthorSurname(), is(biblio2.getFirstAuthorSurname()));
        assertThat(biblio1.getFullAuthors(), hasSize(2));
        assertThat(biblio1.getFullAuthors().get(0).getFirstName(), is(biblio2.getFullAuthors().get(0).getFirstName()));
        assertThat(biblio2.getFullAuthors().get(0).getAffiliations().get(0).getAffiliationString(), is("Stanford"));
        assertThat(biblio1.getFullAuthors().get(1).getFirstName(), is(biblio2.getFullAuthors().get(1).getFirstName()));
        assertThat(biblio2.getFullAuthors().get(1).getAffiliations().get(0).getAffiliationString(), is("Cambridge"));
    }

    @Test
    public void correct_2authors_initial_2_shouldUpdateAuthor() {
        BiblioItem biblio1 = new BiblioItem();
        biblio1.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe", "ULCA"),
            createPerson("J", "Will", "Harward")
        ));

        BiblioItem biblio2 = new BiblioItem();
        biblio2.setFullAuthors(Arrays.asList(
            createPerson("John1", "Doe", "Stanford"),
            createPerson("Jane", "Will", "Berkley")
        ));

        BiblioItem.correct(biblio1, biblio2);

        assertThat(biblio1.getFirstAuthorSurname(), is(biblio2.getFirstAuthorSurname()));
        assertThat(biblio1.getFullAuthors(), hasSize(2));
        assertThat(biblio1.getFullAuthors().get(0).getFirstName(), is(biblio2.getFullAuthors().get(0).getFirstName()));
        assertThat(biblio1.getFullAuthors().get(0).getAffiliations().get(0).getAffiliationString(), is(biblio2.getFullAuthors().get(0).getAffiliations().get(0).getAffiliationString()));
        assertThat(biblio1.getFullAuthors().get(1).getFirstName(), is(biblio2.getFullAuthors().get(1).getFirstName()));
        assertThat(biblio1.getFullAuthors().get(1).getAffiliations().get(0).getAffiliationString(), is(biblio2.getFullAuthors().get(1).getAffiliations().get(0).getAffiliationString()));
    }

    @Test
    @Ignore("This test is failing ")
    public void correct_2authors_initial_shouldUpdateAuthor() {
        BiblioItem biblio1 = new BiblioItem();
        biblio1.setFullAuthors(Arrays.asList(
            createPerson("John", "Doe", "ULCA"),
            createPerson("Jane", "Will", "Harward")
        ));

        BiblioItem biblio2 = new BiblioItem();
        biblio2.setFullAuthors(Arrays.asList(
            createPerson("John1", "Doe", "Stanford"),
            createPerson("J", "Will", "Berkley")
        ));

        BiblioItem.correct(biblio1, biblio2);

        assertThat(biblio1.getFirstAuthorSurname(), is(biblio2.getFirstAuthorSurname()));
        assertThat(biblio1.getFullAuthors(), hasSize(2));
        assertThat(biblio1.getFullAuthors().get(0).getFirstName(), is(biblio2.getFullAuthors().get(0).getFirstName()));
        assertThat(biblio1.getFullAuthors().get(0).getAffiliations().get(0).getAffiliationString(), is("UCLA"));
        assertThat(biblio1.getFullAuthors().get(1).getFirstName(), is(biblio2.getFullAuthors().get(1).getFirstName()));
        assertThat(biblio1.getFullAuthors().get(1).getAffiliations().get(0).getAffiliationString(), is("Berkley"));
    }

    private Person createPerson(String firstName, String secondName) {
        final Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(secondName);
        return person;
    }

    private Person createPerson(String firstName, String secondName, String affiliation) {
        final Person person = createPerson(firstName, secondName);
        final Affiliation affiliation1 = new Affiliation();
        affiliation1.setAffiliationString(affiliation);
        person.setAffiliations(Arrays.asList(affiliation1));
        return person;
    }
}
