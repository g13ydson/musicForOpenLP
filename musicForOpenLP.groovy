def w = new StringWriter()
def xml = new groovy.xml.MarkupBuilder(w)
def json = null
def jsonSlurper = null
def read = javax.swing.JOptionPane.&showInputDialog
def show = javax.swing.JOptionPane.&showMessageDialog 
def linkMusica = read 'Cole o link da música'

if(linkMusica?.trim()){

def link = linkMusica.split("/")

def artista = link[3]
def musica = link[4].split(".html")[0]
    jsonSlurper = new groovy.json.JsonSlurper()
    json = jsonSlurper.parseText(new URL( "http://api.vagalume.com.br/search.php?art=$artista&mus=$musica" ).text)
    
    if(!json.art && !json.mus){
    	show null,'Artista ou Música inválidos, tente novamente','Erro :(',javax.swing.JOptionPane.ERROR_MESSAGE
           System.exit(1)
    }

}else{
   show null,'Erro no Link, tente novamente','Erro :(',javax.swing.JOptionPane.ERROR_MESSAGE
           System.exit(1)
}
//def json = grails.converters.JSON.parse( new URL( 'http://api.vagalume.com.br/search.php?art=U2&mus=one' ).text )

def autor = json.art.name
def titulo = json.mus.name

def letra = json.mus.text.toString().replaceAll("\n","<br/>").split("<br/><br/>")

xml.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")
xml.song(xmlns:"http://openlyrics.info/namespace/2009/song", version:"0.8", createdIn:"OpenLP 1.9.9", modifiedIn:"OpenLP 1.9.9",modifiedDate:"2012-12-30T22:23:58"){
    
 properties(){
     titles(){
         title(titulo)
     }
     authors(){
         author(autor)
     }
 }
 
 lyrics(){
 
         for(int i = 0; i<letra.size();i++){
             verse(name:"v"+(i+1)){
                 lines(){
                     mkp.yieldUnescaped letra[i]
                 }
             }
         }
 }
 
}

print w.toString()
def f = new File(autor+"-"+titulo+".xml")
f.write(w.toString())
show null,'Arquivo criado com sucesso','Desenvolvido por Gleydson Tavares :)',javax.swing.JOptionPane.INFORMATION_MESSAGE
