package io.cplabs.did;

import com.metadium.did.MetadiumWallet;
import com.metadium.did.crypto.MetadiumKey;
import com.metadium.did.exception.DidException;
import com.metadium.did.protocol.MetaDelegator;
import com.metaidum.did.resolver.client.DIDResolverAPI;
import com.metaidum.did.resolver.client.document.DidDocument;
import com.metaidum.did.resolver.client.document.PublicKey;
import com.metaidum.did.resolver.client.document.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        String apiKey = "test_key";
        String delegatorUrl = "https://testnetdelegator.metadium.com";
        String resolverUrl = "https://testnetresolver.metadium.com/1.0/";
        String metadiumUrl = "https://api.metadium.com/dev";
        String didPrefix = "did:meta:testnet";

        try {
            String configFilePath = "./config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            delegatorUrl = prop.getProperty("DELEGATOR_URL");
            resolverUrl = prop.getProperty("RESOLVER_URL");
            metadiumUrl = prop.getProperty("METADIUM_URL");
            didPrefix = prop.getProperty("DID_PREFIX");
            System.out.println("Delegator URL: " + delegatorUrl);
            System.out.println("Resolver URL: " + resolverUrl);
            System.out.println("Metadium RPC URL: " + metadiumUrl);
            System.out.println("DID Prefix: " + didPrefix);
            System.out.println("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MetaDelegator delegator = new MetaDelegator(delegatorUrl, metadiumUrl, didPrefix, apiKey);
        DIDResolverAPI.getInstance().setResolverUrl(resolverUrl);
        try {
            System.out.println(">>>>> DID TEST start <<<<<");
            MetadiumWallet wallet = MetadiumWallet.createDid(delegator);
            MetadiumKey key = wallet.getKey();
            String did = wallet.getDid();
            String kid = wallet.getKid();
            System.out.println("Create DID:");
            System.out.println("  DID = " + did);
            System.out.println("  KID = " + kid);

            DidDocument didDocument = wallet.getDidDocument();
            System.out.println("Get DID Document:");
            System.out.println("   Context: " + didDocument.getContext());
            System.out.println("   Id: " + didDocument.getId());
            System.out.println("   Authentication: " + didDocument.getAuthentication());
            for (PublicKey k: didDocument.getPublicKeyOfAuthentication()) {
                System.out.println("   PublicKeyOfAuthentication: " + k.getPublicKeyHex());
            }
            for (PublicKey k: didDocument.getPublicKey()) {
                System.out.println("   PublicKey: " + k.getPublicKeyHex());
            }
            for (Service k: didDocument.getService()) {
                System.out.println("   Service: " + k.getServiceEndpoint());
            }
        } catch (DidException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(">>>>> DID TEST end <<<<<");
    }
}
