import java.util.HashMap;

public class PROB {
    private HashMap<Integer,Double> gene_prob;
    private HashMap<Integer, HashMap<Boolean,Double>> trait_prob;
    private HashMap<String, Double> mutation_prob;

    //construct probability distribution
    public PROB(){
    //set unconditional probabilities for having gene
        
    HashMap<Integer, Double> geneprob = new HashMap<>();
    geneprob.put(0,0.96);
    geneprob.put(1, 0.03);
    geneprob.put(2, 0.01);
    this.gene_prob = geneprob;

    //set trait probabilities
    
    HashMap<Integer, HashMap<Boolean, Double>> traitprob = new HashMap<>();
    
    //probability of having trait given two copies of genes
    HashMap<Boolean,Double> twogene = new HashMap<>();
    twogene.put(true, 0.65);
    twogene.put(false, 0.35);
    traitprob.put(2,twogene);

    //probability of having trait given one copy of gene
    HashMap<Boolean,Double> onegene = new HashMap<>();
    onegene.put(true, 0.56);
    onegene.put(false, 0.44);
    traitprob.put(1, onegene);

    //probability of having trait given no gene
    HashMap<Boolean,Double> nogene = new HashMap<>();
    nogene.put(true,0.01);
    nogene.put(false, 0.99);
    traitprob.put(0,nogene);
    
    this.trait_prob = traitprob;

    //set mutation probability
    HashMap<String,Double> mutation_prob = new HashMap<>();
    mutation_prob.put("mutation", 0.01);
    this.mutation_prob = mutation_prob;
    }



    //get genetic probability
    public Double get_genetic_probability(int gene_number){
        //Return the genetic probability distribution of having gene_number of gene
        Double probability = this.gene_prob.get(gene_number);
        return probability;
    }

    public Double get_trait_probability(int gene_number,boolean have_trait){
        

        double probability = this.trait_prob.get(gene_number).get(have_trait);
        return probability;
    }

    public Double get_mutation_probability(){
        return this.mutation_prob.get("mutation");
    }
}
