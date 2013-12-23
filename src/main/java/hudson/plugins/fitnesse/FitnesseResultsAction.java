package hudson.plugins.fitnesse;

import hudson.model.AbstractBuild;
import hudson.tasks.test.AbstractTestResultAction;

import org.kohsuke.stapler.StaplerProxy;

import java.net.MalformedURLException;
import java.net.URL;

public class FitnesseResultsAction extends AbstractTestResultAction<FitnesseResultsAction> implements StaplerProxy {
	private static final long serialVersionUID = 1L;
	private final FitnesseResults results;
	private final FitnesseResultsRecorder publisher;
	
	protected FitnesseResultsAction(AbstractBuild<?, ?> owner, FitnesseResultsRecorder publisher, FitnesseResults results) {
		super(owner);
		this.results = results;
		this.publisher = publisher;
		results.setOwner(owner);
	}

	@Override
	public int getFailCount() {
		return results.getFailCount();
	}

	@Override
	public int getTotalCount() {
		return results.getTotalCount();
	}

	@Override
	public int getSkipCount() {
		return results.getSkipCount();
	}

	@Override
	public FitnesseResults getResult() {
		return results;
	}

	/**
	 * {@link Action}
	 */
	@Override
	public String getUrlName() {
		return "fitnesseReport";
	}

	/**
	 * {@link Action}
	 */
	@Override
	public String getDisplayName() {
		return "FitNesse Results";
	}

	/**
	 * {@link Action}
	 */
	@Override
	public String getIconFileName() {
		return "/plugin/fitnesse/icons/fitnesselogo-32x32.gif";
	}

	/** 
	 * {@link StaplerProxy}
	 */
	public Object getTarget() {
		return results;
	}

	/**
	 * Referenced in summary.jelly and FitnesseProjectAction/jobMain.jelly
	 */
	public String getSummary() {
		return String.format("(%s, %d pages: %d wrong or with exceptions, %d ignored)",
				getResult().getName(), getTotalCount(), getFailCount(), getSkipCount());
	}

    public String getLinkFor(String fitnessePage) {
        return getLinkFor(fitnessePage, null);
    }

    public String getLinkFor(String fitnessePage, String hudsonHost) {
        return getLinkFor(fitnessePage, hudsonHost, fitnessePage);
    }

    public String getLinkFor(String fitnessePage, String hudsonHost, String title) {
        String prefix = "";

        if(this.publisher != null){
            prefix = publisher.getDescriptor().getPublicUrl();
        } else {
            if(hudsonHost != null){
                prefix = hudsonHost;
            }
        }

        if(prefix == null || prefix.equals("")){
            return title;
        }
        if(!prefix.endsWith("/")){
            prefix += "/";
        }
        return String.format("<a href=\"%s%s\">%s</a>", prefix, fitnessePage, title);
    }
}
