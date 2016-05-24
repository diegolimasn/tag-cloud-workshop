package br.ufba.matc96.tagcloud;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TagCloudMain
{
	public static void main(String[] args)
	{
		Corpus corpus = new TwitterCorpus("bbcbrasil");
		//Corpus corpus = new BibsonomyCorpus("health");

		//Create tag cloud using spectral clustering
		TagCloudCreator SCCreator = new SCTagCloudCreator(corpus, 5, 20);
		TagCloud SCTagCloud = SCCreator.create();
		
		//Create tag cloud using tag popularity
		TagCloudCreator PCreator = new PopularityTagCloudCreator(corpus, 100);
		TagCloud PTagCloud = PCreator.create();

		//Analyze tag clouds
		TagCloudAnalyzer SCAnalyzer = new TagCloudAnalyzer(SCTagCloud, corpus);
		TagCloudAnalyzer PAnalyzer = new TagCloudAnalyzer(PTagCloud, corpus);

		//Print analysis
		System.out.println("N TAGS: "+corpus.getTags().size()+" N DOCS: "+corpus.getTagDocuments().size());
		System.out.printf("%12s | %12s | %12s | %12s\n", "M�todo", "Cobertura", "Overlap", "Relev�ncia");
		printMetrics("Spec. Clust.", SCAnalyzer);
		printMetrics("Popularidade", PAnalyzer);
		displayTagCloud(SCTagCloud);
		displayTagCloud(PTagCloud);
	}
	
	protected static void displayTagCloud(final TagCloud tagCloud)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
            @Override
            public void run() {
            	initUI(tagCloud.getClusters());
            }
        });
	}
	
	protected static void initUI(Map<String, List<MyTag>> clusters)
	{
        JFrame frame = new JFrame("Tag Cloud Workshop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        
        for (Entry<String, List<MyTag>> c : clusters.entrySet())
        {
        	for(MyTag tag: c.getValue())
        	{
	            final JLabel label = new JLabel(tag.getTagName());
	            label.setOpaque(false);
	            label.setFont(label.getFont().deriveFont(
	            		(float) (Math.log(tag.getTermFrequency())*5)));
	            panel.add(label);
        	}
            final JLabel label = new JLabel(".");
            panel.add(label);
        }
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
	
	private static void printMetrics(String method, TagCloudAnalyzer analyzer)
	{
		System.out.printf("%12s | ", method);
		System.out.printf("%12f | ", analyzer.calcCov());
		System.out.printf("%12f | ", analyzer.calcOverlap());
		System.out.printf("%12f\n", analyzer.calcRelevance());
	}
}