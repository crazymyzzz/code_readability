

public abstract class AbstractHttp11Processor {


    
    /**
     * Initialize standard input and output filters.
     */
    protected void initializeFilters() {
        // Create and add the identity filters.
        getInputBuffer().addFilter(new IdentityInputFilter());
        getOutputBuffer().addFilter(new IdentityOutputFilter());

        // Create and add the chunked filters.
        getInputBuffer().addFilter(new ChunkedInputFilter());
        getOutputBuffer().addFilter(new ChunkedOutputFilter());

        // Create and add the void filters.
        getInputBuffer().addFilter(new VoidInputFilter());
        getOutputBuffer().addFilter(new VoidOutputFilter());

        // Create and add buffered input filter
        getInputBuffer().addFilter(new BufferedInputFilter());

        // Create and add the chunked filters.
        //getInputBuffer().addFilter(new GzipInputFilter());
        getOutputBuffer().addFilter(new GzipOutputFilter());
        
        pluggableFilterIndex = getInputBuffer().getFilters().length;
    }



    /**
     * Add an input filter to the current request.
     *
     * @return false if the encoding was not found (which would mean it is
     * unsupported)
     */
    protected boolean addInputFilter(InputFilter[] inputFilters,
                                     String encodingName) {
        if (encodingName.equals("identity")) {
            // Skip
        } else if (encodingName.equals("chunked")) {
            getInputBuffer().addActiveFilter
                (inputFilters[Constants.CHUNKED_FILTER]);
            contentDelimitation = true;
        } else {
            for (int i = pluggableFilterIndex; i < inputFilters.length; i++) {
                if (inputFilters[i].getEncodingName()
                    .toString().equals(encodingName)) {
                    getInputBuffer().addActiveFilter(inputFilters[i]);
                    return true;
                }
            }
            return false;
        }
        return true;
    }



}
