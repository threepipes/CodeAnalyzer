package analyzer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import cpp.CPP14BaseListener;
import cpp.CPP14Lexer;
import cpp.CPP14Parser;
import cpp.CPP14Parser.ConversiondeclaratorContext;
import cpp.CPP14Parser.ConversionfunctionidContext;
import cpp.CPP14Parser.ForinitstatementContext;
import cpp.CPP14Parser.IterationstatementContext;
import cpp.CPP14Parser.MemberdeclarationContext;
import cpp.CPP14Parser.PostfixexpressionContext;

public class Lexer {
	BufferedReader in;
	public Lexer(File file) {
		try {
			in =new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println("No such file: "+ file);
		}
	}
	
	public List<String> getTokenList(){
		try{
			StringBuilder sb = new StringBuilder();
			for(String line=in.readLine(); line!=null; line=in.readLine()){
				sb.append(line).append('\n');
			}
			in.close();
			return lexer(sb.toString().replace("\\\n", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void outputResult(String outfile){
		try{
			StringBuilder sb = new StringBuilder();
			for(String line=in.readLine(); line!=null; line=in.readLine()){
				sb.append(line).append('\n');
			}
			in.close();
			List<String> tokens = lexer(sb.toString());
			PrintWriter out = new PrintWriter(
					new BufferedWriter(
							new FileWriter(outfile)));
			for(String t: tokens){
				out.println(t);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void test(){
		try{
			StringBuilder sb = new StringBuilder();
			for(String line=in.readLine(); line!=null; line=in.readLine()){
				sb.append(line).append('\n');
			}
			in.close();
			parser(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<String> lexer(String seq){
		CPP14Lexer lex = new CPP14Lexer(new ANTLRInputStream(seq));
		CommonTokenStream cts = new CommonTokenStream(lex);
		List<String> tokens = new ArrayList<>();
		cts.fill();
		for(Token t: cts.getTokens()){
			tokens.add(t.getText());
		}
		return tokens;
	}
	
	private void parser(String seq){
		CPP14Lexer lex = new CPP14Lexer(new ANTLRInputStream(seq));
		CommonTokenStream cts = new CommonTokenStream(lex);
		CPP14Parser psr = new CPP14Parser(cts);
		SimpleListener lsn = new SimpleListener();
//		cts.fill();
//		System.out.println(cts.getTokens().size());
		psr.addParseListener(lsn);
		psr.translationunit().enterRule(lsn);
		
	}
	
	class SimpleListener extends CPP14BaseListener{
		public SimpleListener() {
			
		}
		@Override
		public void enterIterationstatement(IterationstatementContext ctx) {
			System.out.println("ite: "+ctx.start);
			super.enterIterationstatement(ctx);
		}
		@Override
		public void enterForinitstatement(ForinitstatementContext ctx) {
			System.out.println("for: "+ctx.start);
			super.enterForinitstatement(ctx);
		}
		@Override
		public void enterConversiondeclarator(ConversiondeclaratorContext ctx) {
//			System.out.println(ctx.getStart());
			super.enterConversiondeclarator(ctx);
		}
		@Override
		public void enterConversionfunctionid(ConversionfunctionidContext ctx) {
//			System.out.println(ctx.getStart());
			super.enterConversionfunctionid(ctx);
		}
		@Override
		public void enterMemberdeclaration(MemberdeclarationContext ctx) {
//			System.out.println(ctx.start.getText());
//			Interval interval = ctx.getSourceInterval(); 
//			System.out.println(interval);
//			System.out.println(ctx.getStart());
			super.enterMemberdeclaration(ctx);
		}
		@Override
		public void enterPostfixexpression(PostfixexpressionContext ctx) {
//			String space = "";
//			for(int i=0; i<ctx.depth(); i++) space += " ";
//			Interval interval = ctx.getSourceInterval(); 
//			System.out.println(interval);
			super.enterPostfixexpression(ctx);
		}
	}
}
