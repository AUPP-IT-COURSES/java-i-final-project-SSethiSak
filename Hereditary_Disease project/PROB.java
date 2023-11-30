import java.math.BigDecimal;
import java.util.HashMap;

public class PROB {
    private HashMap<Integer,BigDecimal> gene_prob;
    private HashMap<Integer, HashMap<Boolean,BigDecimal>> trait_prob;
    private HashMap<String, BigDecimal> mutation_prob;



    //construct probability distribution
    public PROB(){
    //set unconditional probabilities for having gene
        
    HashMap<Integer, BigDecimal> geneprob = new HashMap<>();
    geneprob.put(0,new BigDecimal(0.96));
    geneprob.put(1, new BigDecimal(0.03));
    geneprob.put(2, new BigDecimal(0.01));
    this.gene_prob = geneprob;

    //set trait probabilities
    
    HashMap<Integer, HashMap<Boolean, BigDecimal>> traitprob = new HashMap<>();
    
    //probability of having trait given two copies of genes
    HashMap<Boolean,BigDecimal> twogene = new HashMap<>();
    twogene.put(true, new BigDecimal(0.65));
    twogene.put(false, new BigDecimal(0.35));
    traitprob.put(2,twogene);

    //probability of having trait given one copy of gene
    HashMap<Boolean,BigDecimal> onegene = new HashMap<>();
    onegene.put(true, new BigDecimal(0.56));
    onegene.put(false, new BigDecimal(0.44));
    traitprob.put(1, onegene);

    //probability of having trait given no gene
    HashMap<Boolean,BigDecimal> nogene = new HashMap<>();
    nogene.put(true,new BigDecimal(0.01));
    nogene.put(false, new BigDecimal(0.99));
    traitprob.put(0,nogene);
    
    this.trait_prob = traitprob;

    
    //set mutation probability
    HashMap<String,BigDecimal> mutation_prob = new HashMap<>();
    mutation_prob.put("mutation", new BigDecimal(0.01));
    this.mutation_prob = mutation_prob;
    }



    //get genetic probability
    public BigDecimal get_genetic_probability(int gene_number){
        //Return the genetic probability distribution of having gene_number of gene
        BigDecimal probability = this.gene_prob.get(gene_number);
        return probability;
    }

    public BigDecimal get_trait_probability(int gene_number,boolean have_trait){
        

        BigDecimal probability = this.trait_prob.get(gene_number).get(have_trait);
        return probability;
    }

    public BigDecimal get_mutation_probability(){
        return this.mutation_prob.get("mutation");
    }
}
