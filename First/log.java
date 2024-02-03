public class log {
    public int day;
    public Rabbit sampleRabbit;
    public String sampleRabbitString;
    public int populationAtEndOfDay;
    public int pelletsRemaining;
    public int oldestRabbit, oldestGeneration, newestGeneration;

    //depricated 5/10/2023. Use other constructor instead
    @Deprecated
    public log(int day, Rabbit sample, int population, int pellets){ 
        this.day = day;
        sampleRabbit = sample;
        populationAtEndOfDay = population;
        pelletsRemaining = pellets;
        sampleRabbitString = "";
    }

    //depricated 5/24/2023. Use other constructor instead
    @Deprecated
    public log(int day, String sample, int population, int pellets){
        this.day = day;
        sampleRabbitString = sample;
        populationAtEndOfDay = population;
        pelletsRemaining = pellets;
    }


    public log(int day, String sample, int population, int pellets, int oldestRabbit, int oldestGeneration, int newestGeneration){
        this.day = day;
        sampleRabbitString = sample;
        populationAtEndOfDay = population;
        pelletsRemaining = pellets;
        this.oldestGeneration = oldestGeneration;
        this.oldestRabbit = oldestRabbit;
        this.newestGeneration = newestGeneration;
    }

    public log(int day, Rabbit sample, int population, int pellets, int oldestRabbit, int oldestGeneration, int newestGeneration){
        this.day = day;
        sampleRabbitString = sample.toString();
        populationAtEndOfDay = population;
        pelletsRemaining = pellets;
        this.oldestGeneration = oldestGeneration;
        this.oldestRabbit = oldestRabbit;
        this.newestGeneration = newestGeneration;
    }
    
    //toString() method, vital for output
    @Override
    public String toString(){
        return "Day:" + day + 
        " Population:" + populationAtEndOfDay + 
        " Pellets:" + pelletsRemaining + 
        " Rabbit[" + sampleRabbitString + "]";
    }

    /*
     * Output the data to a csv file will be easier for data visualization
     * Possible csv files:
     * Map vis
     * Report (Day by Population, oldest, youngest, etc)
     */
}
