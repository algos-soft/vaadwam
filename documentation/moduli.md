#Moduli
Ogni modulo rappresenta una 'collection' del mongoDB
Tipicamente è composto di (almeno) 4 classi

1) Prova
2) ProvaRepository
3) ProvaService
4) ProvaDialog
5) ProvaList

##Wizard
Le classi vengo costruite automaticamente dal Wizard, disponibile al developer.
Vengono costruite con un scheletro al minimo a cui si può (ovviamente) aggiungere qualsiasi metodo.
Ogni classe ha un flag per permettere di sovrascriverla in caso di futuri passaggi del Wizard (per aggiornare i metodi)
oppure per preservarla da modifiche automatiche

Wiward si preoccupa anche di costruire le costanti per i tag delle classi e di inserirle nel RouterLink.

###1 - Entity
Prova col nome del modulo senza suffissi. Prototype. Estende AEntity. Tracciato record della 'collection'.

###2 - Repository
ProvaRepository nome del modulo col suffisso Repository. Singleton. Estende la MongoRepository. 

###3 - Service
ProvaService nome del modulo col suffisso. Service. Singleton. Estende AService. Business logic.

###4 - Dialog
ProvaDialog nome del modulo col suffisso Dialog. Prototype. Estende AViewDialog. Singolo entityBean.

###5 - List
ProvaList nome del modulo col suffisso List. Gestita da @Route. Estende AGridViewList. Presenta una Grid 