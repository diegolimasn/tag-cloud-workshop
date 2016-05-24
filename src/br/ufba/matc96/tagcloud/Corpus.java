package br.ufba.matc96.tagcloud;

import java.util.Map;
import java.util.List;

public interface Corpus
{
	public List<MyTagDocument> getTagDocuments();
	public Map<String, MyTag> getTags();
}