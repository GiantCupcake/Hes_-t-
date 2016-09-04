-----------------------------------------------------------------------------------	
Contexte
-----------------------------------------------------------------------------------	

Vous avez developpé sous windows
Vous avez creé des scripts .sh sous windows

-----------------------------------------------------------------------------------	
But
-----------------------------------------------------------------------------------	

Vous aimeriez deployer sous linux

-----------------------------------------------------------------------------------	
Probleme potentiel
-----------------------------------------------------------------------------------	

Les line feed (LF) sous windows ne sont pas les mêmes que sous linux.
Pour cette raison, vos scripts .sh edités sous windows risquent de ne pas fonctionner sous linux directement.

-----------------------------------------------------------------------------------	
Solution 1
-----------------------------------------------------------------------------------	

sed --in-place 's/\r//g' myScript.sh

-----------------------------------------------------------------------------------	
Solution 2
-----------------------------------------------------------------------------------	

Etape 1 :

	Installer git sur la machine de destination
	
		sudo apt-get update
		sudo apt-get install git
		
		et éventuellement
		
		sudo apt-get upgrade
		
		
Etape 2:

	Cloner votre workspace sur la machine de destination avec git
	
Etape 3:

	Recuperer le folder Deploy et effacer tout le reste 
	Evidemment : ne commiter et pusher rien après l'effacement!

-----------------------------------------------------------------------------------	
Note:
-----------------------------------------------------------------------------------	
	
N'oubliez pas de rendre vos scripts executable
	
		chmod a+x ./myScript.sh
	
-----------------------------------------------------------------------------------		
End
-----------------------------------------------------------------------------------	