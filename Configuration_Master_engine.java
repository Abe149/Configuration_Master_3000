import java.io.*;
import java.util.Hashtable;
import java.util.regex.*;
  

public class Configuration_Master_engine {

  private Hashtable<String, Integer> maturityLevel_aliases;

  private int get_maturityLevel_integer_from_alias(String alias_in) {
    return maturityLevel_aliases.get(alias_in);
  }

  private class tuple_for_key_of_a_config {
    public maturityLevel_comparison_types the_MLC;
    public int                            the_maturity_level_to_which_to_compare;
    public String                         the_namespace;
    public String                         the_key; // confusing, innit?  ;-)

    tuple_for_key_of_a_config(maturityLevel_comparison_types MLC_in, int maturity_level_in, String namespace_in, String key_in) { // ctor
      the_MLC                                = MLC_in;
      the_maturity_level_to_which_to_compare = maturity_level_in;
      the_namespace                          = namespace_in;
      the_key                                = key_in;
    }

    public String toString() { // for debugging etc.
      return " tuple_for_key_of_a_config<the_MLC=" + the_MLC + ", the_maturity_level_to_which_to_compare=" + the_maturity_level_to_which_to_compare + ", the_namespace=" + stringize_safely(the_namespace) + ", the_key=" + stringize_safely(the_key) + "> ";
    }
  }


  private class tuple_for_key_of_a_schema {
    public String                         the_namespace;
    public String                         the_key; // confusing, innit?  ;-)

    tuple_for_key_of_a_schema(String namespace_in, String key_in) { // ctor
      the_namespace                          = namespace_in;
      the_key                                = key_in;
    }

    public String toString() { // for debugging etc.
      return " tuple_for_key_of_a_schema<the_namespace=" + stringize_safely(the_namespace) + ", the_key=" + stringize_safely(the_key) + "> ";
    }
  }


  private class semiParsed_line_for_a_config___values_are_all_Strings {
    public tuple_for_key_of_a_config key;
    public String                    value;
    semiParsed_line_for_a_config___values_are_all_Strings(tuple_for_key_of_a_config key_in, String value_in) {
      key   =   key_in;
      value = value_in;
    }
    public String toString() { // for debugging etc.
      return " semiParsed_line_for_a_config___values_are_all_Strings<key=" + key + ", value=" + stringize_safely(value) + "> ";
    }
  }


  private class parsed_line_for_a_schema {
    public tuple_for_key_of_a_schema key;
    public value_types               value;
    parsed_line_for_a_schema(tuple_for_key_of_a_schema key_in, value_types value_in) {
      key   =   key_in;
      value = value_in;
    }
    public String toString() { // for debugging etc.
      return " parsed_line_for_a_schema<key=" + key + ", value=" + value + "> ";
    }
  }


  private Hashtable<String                   , value_types> typenames_to_types; // unfortunately, initializing a hashtable in Java is a {can of worms / Pandora`s box}, so we`ll do it the old-fashioned way

  private Hashtable<tuple_for_key_of_a_schema, value_types> the_schema;

  private static String stringize_safely(String input) {
    if (null == input)  return "«null»";
    return "“" + input + "”";
  }


  parsed_line_for_a_schema parse_a_line_for_a_schema(String line) throws IOException {
    String                         the_namespace = null;
    String                         the_key       = null;
    String                         the_value     = null;

    line = line.trim();
    if (line.length() > 0 && '#' != line.charAt(0)) { // ignore whole-line-possibly-modulo-leading-space comments
      line = line.replaceFirst("⍝.*", "").trim(); // HARD-CODED: the APL "lamp" symbol for an until-end-of-line comment, AKA "APL FUNCTIONAL SYMBOL UP SHOE JOT"
      final String[] the_split = line.split("␟"); // HARD-CODED: Unicode visible character for ASCII control "character" UNIT SEPARATOR

      // TO DO: make this fail more elegantly when the number of split results is not as expected

      the_namespace = the_split[0].trim();
      the_key       = the_split[1].trim();
      the_value     = the_split[2].trim();
    }

    if (null == the_namespace || the_namespace.length() < 1 || null == the_key || the_key.length() < 1 || null == the_value || the_value.length() < 1)  return null;

    return new parsed_line_for_a_schema(new tuple_for_key_of_a_schema(the_namespace, the_key), typenames_to_types.get(the_value)); // TO DO: make this fail gracefully when the typename "value" is unknown/unrecognized
  }


  semiParsed_line_for_a_config___values_are_all_Strings semiparse_a_line_for_a_config(String line) throws IOException {
    maturityLevel_comparison_types the_MLC = maturityLevel_comparison_types.equal_to;
    int                            the_maturity_level_to_which_to_compare = -1;
    String                         the_namespace = null;
    String                         the_key       = null;
    String                         the_value     = null;

    line = line.trim();
    if (line.length() > 0 && '#' != line.charAt(0)) { // ignore whole-line-possibly-modulo-leading-space comments
      line = line.replaceFirst("⍝.*", "").trim(); // HARD-CODED: the APL "lamp" symbol for an until-end-of-line comment, AKA "APL FUNCTIONAL SYMBOL UP SHOE JOT"
      final String[] the_split = line.split("␟"); // HARD-CODED: Unicode visible character for ASCII control "character" UNIT SEPARATOR

      // TO DO: make this fail more elegantly when the number of split results is not as expected
      final String the_MLC_and_integer_as_a_string = the_split[0].trim();
      final char the_MLC_as_a_char = the_MLC_and_integer_as_a_string.charAt(0);
      switch (the_MLC_as_a_char) {
        case '<': the_MLC = maturityLevel_comparison_types.less_than;
          break;
        case '≤': the_MLC = maturityLevel_comparison_types.less_than_or_equal_to;
          break;
        case '=': the_MLC = maturityLevel_comparison_types.equal_to;
          break;
        case '≥': the_MLC = maturityLevel_comparison_types.greater_than_or_equal_to;
          break;
        case '>': the_MLC = maturityLevel_comparison_types.greater_than;
          break;
        default:
          throw new IOException("Syntax error: unrecognized leading character in a maturity-level specification.");
        }
      the_maturity_level_to_which_to_compare = Integer.parseInt(the_MLC_and_integer_as_a_string.substring(1).trim());

      the_namespace = the_split[1].trim();
      the_key       = the_split[2].trim();
      the_value     = the_split[3].trim();
    }

    return new semiParsed_line_for_a_config___values_are_all_Strings(new tuple_for_key_of_a_config(the_MLC, the_maturity_level_to_which_to_compare, the_namespace, the_key), the_value);
  }


  Configuration_Master_engine(BufferedReader maturityLevel_aliases_input, BufferedReader[] schema_inputs, BufferedReader[] config_inputs, int verbosity) throws IOException { // start of ctor

    // there should be a more-elegant way to do this...
    typenames_to_types = new Hashtable<String, value_types>();
    typenames_to_types.put("integer"            , value_types.integer);
    typenames_to_types.put("nonnegative_integer", value_types.nonnegative_integer);
    typenames_to_types.put("positive_integer"   , value_types.positive_integer);
    typenames_to_types.put("nonempty_string"    , value_types.nonempty_string);
    typenames_to_types.put("string"             , value_types.string);
    typenames_to_types.put("URL"                , value_types.URL);

    maturityLevel_aliases = new Hashtable<String, Integer>();

    try {

      while (maturityLevel_aliases_input.ready()) {
        String line = maturityLevel_aliases_input.readLine();
        if (verbosity > 5) {
          System.err.println("TESTING 1: maturity-level aliases input line: ''" + line + "''");
        }
        line = line.split("#")[0]; // discard comments
        if (verbosity > 5) {
          System.err.println("TESTING 2: maturity level aliases input line after discarding comments: ''" + line + "''");
        }
        line = line.replace(" ", "").toLowerCase(); // this algorithm will result in some "unexpected interpretations" for seemingly-invalid inputs, e.g. "d e v =" is equivalent to "dev=" and "1 2 3 4 5" is equivalent to "12345"
        if (verbosity > 5) {
          System.err.println("TESTING 3: maturity level aliases input line after removing all ASCII spaces and lower-casing: ''" + line + "''");
        }
        if (line.length() > 0) {
          Matcher m1 = Pattern.compile("(\\p{javaLowerCase}+)=(\\d+).*").matcher(line); // allows trailing "garbage"
          if (verbosity > 5) {
            System.err.println("TESTING 4: m1: " + m1);
            System.err.println("TESTING 5: m1.groupCount() -> " + m1.groupCount());
          }

          if (verbosity > 5) {
            System.err.println("TESTING 6: m1: " + m1);
          }
          final boolean line_matched_the_regex = m1.find(); // CRUCIAL
          if (verbosity > 5) {
            System.err.println("TESTING 7: line_matched_the_regex: " + line_matched_the_regex);
          }

          if (line_matched_the_regex) {

            if (verbosity > 5) {
              System.err.println("TESTING 8: m1: " + m1);
            }

            MatchResult mr1 = m1.toMatchResult();
            if (verbosity > 5) {
              System.err.println("TESTING  9: mr1: " + mr1);
              System.err.println("TESTING 10: mr1.groupCount() -> " + mr1.groupCount());
            }

            if (mr1.groupCount() != 2) {
              throw new IOException("Wrong number of groups in results for maturity-level aliases input line micro-parser: expected 2, got " + String.valueOf(mr1.groupCount()));
            }

            final String            alias = m1.group(1);
            if (verbosity > 4) {
              System.err.println("TESTING 11: alias=''" + alias + "''");
            }
            final String number_as_string = m1.group(2);
            final int number = Integer.parseInt(number_as_string);
            if (verbosity > 4) {
              System.err.println("TESTING 12: number: " + number);
              System.err.println();
            }
            if (number < 0) {
              throw new IOException("Negative number in maturity-level aliases: for alias ''" + alias + "'', got " + String.valueOf(number));
            }

            maturityLevel_aliases.put(alias, number);

          } /* if line_matched_the_regex */ else {
            throw new IOException("Syntax error in maturity-level aliases: ''" + line + "''");
          }

        } // if line.length() > 0

      } // end while maturityLevel_aliases_input.ready()

      if (verbosity > 1) {
        System.err.println("INFO: maturityLevel_aliases: " + maturityLevel_aliases);
        System.err.println();
      }

      the_schema = new Hashtable<tuple_for_key_of_a_schema, value_types>();
      for (BufferedReader schema_input : schema_inputs) {
        while (schema_input.ready()) {
          String line = schema_input.readLine();
          if (verbosity > 1) {
            System.err.println("TESTING 13: schema input line: ''" + line + "''");

            parsed_line_for_a_schema parse_result = parse_a_line_for_a_schema(line);
            System.err.println("TESTING 14: schema line partial parse: " + parse_result);

            if (null == parse_result || null == parse_result.key || null == parse_result.key.the_namespace || null == parse_result.key.the_key || null == parse_result.value) {
              if (verbosity > 2) {
                System.err.println("TESTING 15: schema line partial parse indicates not a line with valid data, e.g. an effectively-blank or all-comment line");
              }
            } else { // looks like a valid line
              if (verbosity > 2) {
                System.err.println("TESTING 16: schema line partial parse indicates a line with valid data!  Hooray!!!");
              }
            }

          } // end if verbosity > 1
        } // end while
      } // end for BufferedReader schema_input : schema_inputs

      if (verbosity > 0) {
        System.err.println("the_schema: " + the_schema);
      }

// saved for later: if (parse_result.key.the_maturity_level_to_which_to_compare < 0 || null == parse_result.key.the_namespace || null == parse_result.key.the_key || null == parse_result.value) {

    } catch (IOException ioe) {

       final String response = "An I/O exception occurred while trying to initialize the Configuration Master engine: " + ioe;
       System.err.println("\033[31m" + response + "\033[0m");
       throw new IOException(response); // enabling the following snippet didn`t seem to add anything perceptible: , ioe.getCause());
    } // end of try-catch

  } // end of ctor




} // end of class "Configuration_Master_engine"
