/*
 * Author:
 * Rafael Oliveira Ledo
 *
 * License: GPLv2.0
 *
 * It's used for Company Technical Challenges
 * You'll need to rewrite to use.
 *
 * Please don't copy.
 *
 * CAUTION!
 *  Only remove this comment block in case of TOTAL REWRITE
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class HTMLAnalyzer {

  private static class Context {
    static ArrayList<String> tokens = new ArrayList<>();
    static String currentToken = "";
    static boolean tagOpen = false;
		static ArrayList<String> children = new ArrayList<>();
  }

  /**
   * @description
   *              Lida com inputs do usuário. Suporta URL e arquivos locais.
   * @note
   *              Foi criada a capacidade de lidar com arquivos locais apenas para
   *              testes offline de desenvolvimento. O programa continua não imprimindo
   *              nada em casos de uso com arquivos locais.
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("Usage: java HTMLAnalyzer <URL>");
      return;
    }
    String input = args[0];

    InputStream inputStream = null;

    try {
      if (input.startsWith("http://") || input.startsWith("https://")) {
        URI uri = new URI(input);
        URL url = uri.toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
          System.out.println("URL connection error");
          return;
        }

        inputStream = connection.getInputStream();
      } else {
        File file = new File(input);
        if (!file.exists() || !file.isFile()) {
          return;
        }
        inputStream = new FileInputStream(file);
      }

      run(inputStream);
      String ht = findHighestNestedText();

			// batch the illegal characters desired
      postProcessTokensForErrors("/");
      postProcessTokensForErrors(".");
			// i haven't tested for all encodings and multiple chars
			// it's really time consuming

			// the highest depth token (HDT)
			System.out.printf("%s", ht);
    } catch (Exception e) {
      System.out.printf("%s", e.getMessage());
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (Exception e) {
      }
    }
  }

  /**
   * @description
   *              Executa a aplicação.
   */
  private static void run(InputStream fis) throws Exception {
    for (int numericByte; (numericByte = fis.read()) != -1;) {
      String hexByte = Integer.toHexString(numericByte);

      checkForErrors(hexByte);

      if (isOpeningArrow(hexByte)) {
        if (Context.currentToken.length() > 0) {
          addTokenIfNotBlank(Context.currentToken);
          Context.currentToken = "";
        }
        Context.tagOpen = true;
        continue;
      }

      if (isTagOpen() && isClosingArrow(hexByte)) {
        Context.tagOpen = false;
        addTokenIfNotBlank(Context.currentToken);
        Context.currentToken = "";
        continue;
      }

      if (isLineFeed(hexByte)) {
        continue;
      }

      Context.currentToken += (char) numericByte;
    }
    addTokenIfNotBlank(Context.currentToken);
  }

  /**
   * @description
   *              Checa por erros e joga uma exceção com texto personalizado.
   *              Esta é gerenciada por qualquer método. Ocorre, neste programa,
   *              no método main.
   */
  private static void checkForErrors(String hexByte) throws Exception {
    boolean doubleOpeningArrows = isTagOpen() && isOpeningArrow(hexByte);
    boolean doubleClosingArrows = !isTagOpen() && isClosingArrow(hexByte);
    if (doubleClosingArrows || doubleOpeningArrows) {
      throw new Exception("malformed HTML");
    }
  }

  /**
   * @description
   *              Retorna se o byte especificado é `<`.
   */
  private static boolean isOpeningArrow(String hexByte) {
    return hexByte.equals("3c");
  }

  /**
   * @description
   *              Retorna se o byte especificado é `\n`.
   */
  private static boolean isLineFeed(String hexByte) {
    return hexByte.equals("a");
  }

  /**
   * @description
   *              Retorna se atualmente há uma tag `<` aberta.
   */
  private static boolean isTagOpen() {
    return Context.tagOpen;
  }

  /**
   * @description
   *              Retorna se o token recebido é uma Tag HTML. Os tokens
   *              armazenados foram "trimmados". Além disso, existe a
   *              premissa de que essas tags não possuem espaço e parâmetros.
   *              Do contrário, este método seria maior.
   */
  private static boolean isTag(String token) {
    return !token.contains(" ") && !isTagOpen();
  }

  /**
   * @description
   *              Retorna se o byte do parâmetro é `>`.
   */
  private static boolean isClosingArrow(String hexByte) {
    return hexByte.equals("3e");
  }

  /**
   * @description
   *              Adiciona o token na lista de tokens após processamento
   *              posterior.
   */
  private static void addTokenIfNotBlank(String token) {
    if (token.trim().isEmpty())
      return;
    Context.tokens.add(token.trim());
  }

  /**
   * @description
   *              Procura pelo nível mais profundo do HTML e imprime na tela.
   */
  private static String findHighestNestedText() {
    int maxDepth = -1;
    String highestText = null;
    int currentDepth = 0;

    for (String token : Context.tokens) {
      if (token.startsWith("/")) {
        currentDepth--;
      } else if (isTag(token)) {
        currentDepth++;
      } else {
				Context.children.add(token);
        if (currentDepth > maxDepth) {
          maxDepth = currentDepth;
          highestText = token;
        } else if (currentDepth == maxDepth && highestText == null) {
          highestText = token;
        }
      }
    }

    if (highestText != null) {
			return highestText;
    }
		return null;
  }

  /**
   * @description
   *                Faz pós-processamento dos token mal-formados
   */
  private static void postProcessTokensForErrors(String illegalChar) throws Exception {
		boolean hasDoubleIllegalInStart = false, hasDoubleIllegalInEnd = false,
						hasIllegalWithin = false, isChildrenToken = false;

		// early throw for additional tests
		if (!illegalChar.equals("/")) {
			String token = "";
			for (int i = 0; i < Context.tokens.size(); i++) {
				token = Context.tokens.get(i);
				isChildrenToken = Context.children.contains(token);
				if (isChildrenToken) continue;
				if (token.contains(illegalChar)) {
					throw new Exception("malformed HTML");
				}
			}
		}

		String currentCombination = "";
		currentCombination += illegalChar;

		String token = "";
		for (int i = 0; i < Context.tokens.size(); i++) {
			token = Context.tokens.get(i);
			isChildrenToken = Context.children.contains(token);
			currentCombination = illegalChar;
			if (isChildrenToken) continue;
			for (int j = 0; j < token.length(); j++) {
				currentCombination += illegalChar;

				hasDoubleIllegalInStart =
					token.startsWith(illegalChar) &&
					token.contains(currentCombination);

				hasDoubleIllegalInEnd =
					token.endsWith(illegalChar) &&
					token.contains(currentCombination);

				hasIllegalWithin =
					(!token.startsWith(illegalChar) && !token.endsWith(illegalChar))
					&& (token.contains(illegalChar) || token.contains(currentCombination));

				if (hasDoubleIllegalInStart || hasDoubleIllegalInEnd ||
							hasIllegalWithin) {
					throw new Exception("malformed HTML");
				}
			}
		}
  }

  /**
   * @description
	 *							HELPER FUNCTION
   *              Imprime todos os tokens sem processamento.
   */
  private static void printTokens() {
    System.out.println("Tokens: \n");
    for (String token : Context.tokens) {
      System.out.println(token);
    }
  }

	/**
	 * @description
	 *							HELPER FUNCTION
	 *							Imprime todos os tokens que sao filhos de tags
	 */
	private static void printChildren() {
    System.out.println("\nChildren Tokens: \n");
    for (String token : Context.children) {
      System.out.println(token);
    }
	}
}
