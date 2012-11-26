<?php


function sendToHost($url,$data, $data1, $data2, $data3){  
  $url=parse_url($url);   
  $fp = fsockopen($url['host'], 80);   
  $taille = strlen($data)+strlen($data1)+strlen($data2)+strlen($data3);
  //echo $taille;
  $mess= 
    "POST ".$url['path']." HTTP/1.0\r\n". 
    "Host: ".$url['host']."\r\n".   
    "Content-Type: application/x-www-form-urlencoded\r\n". 
    "Content-Length:  ".$taille." \r\n".          
    "\r\n". 
	//"ligne=1&p=15&I=c006qkx&s_ligne=Valider \r\n"; 
    $data."&".$data1."&".$data2."&".$data3."\r\n"; 
	
	
	/*$mess =
	"POST /index.php HTTP/1.1".
"Host: tam.mobitrans.fr".
"Connection: keep-alive".
"Referer: http://tam.mobitrans.fr/index.php?p=13&m=1&I=c006qhy&rd=2347".
"Content-Length: 38".
"Cache-Control: max-age=0".
"Origin: http://tam.mobitrans.fr".
"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1".
"Content-Type: application/x-www-form-urlencoded".
"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8".
"Accept-Encoding: gzip,deflate,sdch".
"Accept-Language: fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4".
"Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3";*/
	
  fputs($fp, $mess);     
  $buf=""; 
  while (!feof($fp)) { 
      $buf .= fgets($fp,128); 
  }   
  fclose($fp); 
  return $buf; 
} 

$id = "c006qhy";
//lecture de la page
$page = file_get_contents("http://tam.mobitrans.fr/index.php?p=13&m=1&I=".$id."&rd=2347");

//<option value="1">Ligne 1</option>
//$page =str_replace('<option value="', "#", $page);
//$page =str_replace('">Ligne', "#", $page);

preg_match_all("#<option[^>]*>(.*)</option>#Ui",$page,$resultat);

//ouverture fichier TAM
// 1 : on ouvre le fichier
$monfichier = fopen('bdTam.sql', 'w+');

//suppression des anciennes donnees
fputs($monfichier, "delete from ligne; \n");
fputs($monfichier, "delete from arret; \n");

//echo 'delete from ligne;'

foreach ($resultat as $r)
{
 
	//$t = $r[0];
	foreach ($r as $t)
	{
		$tab = preg_split("/[\s,]+/",$t);
		if ($tab[0] == "Ligne")
		{
			echo $tab[1]."<br>";
			fputs($monfichier, "insert into ligne (num_ligne) values('".$tab[1]."'); \n"); 
			$res=sendToHost("http://tam.mobitrans.fr/index.php",'ligne='.$tab[1], "I=".$id, "p=15", "s_ligne=Valider"); 
			$res = str_replace("id","<option>",$res);
			$res = str_replace("&m","</option>",$res);
			
			
			
			//echo $res;
			
			preg_match_all("#<option[^>]*>(.*)</option>#Ui",$res,$resultat1);

			
			
			/*$res = str_replace("favoris</a>", "", $res);
			$res = str_replace("services</a>", "", $res);
			$res = str_replace("info</a>", "", $res);
			$res = str_replace("recherche</a>", "", $res);
			$res = str_replace("Contacts</a>", "", $res);
			$res = str_replace("...</a>", "", $res);
			$res = str_replace("Accueil</a>", "", $res);
			$res = str_replace("info</a>", "", $res);*/
			$res = str_replace('<a',"",$res);
			$res = str_replace('<span class="white"> - </span>',"",$res);
			
			
			$res = str_replace('class="white">',"<options>",$res);
			$res = str_replace("</a>","</options>",$res);
			preg_match_all('#<options[^>]*>(.*)</options>#U',$res,$resultat2);
			//foreach ($resultat2 as $r2)
			//{		
 
				$r2 = $resultat2[0];
				/*foreach ($r2 as $t2)
				{
					echo $t2.'<br>';
				}*/
				
				//			foreach ($resultat1 as $r1)
			//{		
				/*$r1 = $resultat1[1];
				foreach ($r1 as $t1)
				{
					echo str_replace("=","",$t1).'<br>';
				}*/
				
				$i=0;
				$r2 = $resultat2[1];
				$r1 = $resultat1[1];
				for($i=0;$i<count($r1);$i++)
				{
					$id_arret = str_replace("=","",$r1[$i]);
					$nom_arret = $r2[$i];
					$nom_arret = str_replace("'"," ",$nom_arret);
					$id_ligne = $tab[1];
					echo $id_arret.' '.$nom_arret.'<br>';
					fputs($monfichier, "insert into arret (num_ligne, id_arret, nom_arret) values('".$id_ligne."', '".$id_arret."', '".$nom_arret."'); \n"); 
				}
				
			//}
		}
		
	}
	
	
}

fclose($monfichier);

/*


POST /index.php HTTP/1.1

Host: tam.mobitrans.fr

Connection: keep-alive

Referer: http://tam.mobitrans.fr/index.php?p=13&m=1&I=c006qhy&rd=2347

Content-Length: 38

Cache-Control: max-age=0

Origin: http://tam.mobitrans.fr

User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1

Content-Type: application/x-www-form-urlencoded

Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8

Accept-Encoding: gzip,deflate,sdch

Accept-Language: fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4

Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.3



http://tam.mobitrans.fr/index.php

*/

















?>