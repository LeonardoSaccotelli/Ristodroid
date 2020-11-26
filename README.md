# Ristodroid
Sviluppo di un'applicazione Android nativa nell'ambito della ristorazione. 
Il progetto prevede la realizzazione di due applicazioni destinate rispettivamente al cliente e al cameriere.
Tramite l'applicazione destinata al cliente, installata su dispositivi in dotazione del ristorante, si potrà consultare il menù, 
aggiungere e rimuove piatti dall'ordine, visualizzare il conto qualora fosse disponibile, visualizzare e inserire recensioni
per un determinato piatto. Ad ordine completato, il cliente confermerà inviando l'ordine all'applicazione del cameriere tramite NFC.
Il cameriere potrà visualizzare e modificare l'ordine, inserire il numero del tavolo, il numero dei coperti, 
la tipologia dei coperti ed eventuali note aggiuntive. A questo punto l'ordine sarà confermato ed inviato. 
Il progetto è stato realizzato nell'ambito dell'esame Mobile Software Development.

## Struttura progetto
* Nella cartella ```database``` è presente il file ```ristodroid.sql``` contenente la definizione del database relazionale sviluppato in MySql
* Nella cartella ```app``` è presente tutto il codice relativo all'applicazione del cliente
* Nella cartella ```waiter``` è presente tutto il codice relativo all'applicazione del cameriere
* Nella cartella ```script php server/Ristodroid/Service``` sono presenti gli script php per comunicare con il server remoto
* Nella cartella ```doc``` è presente una breve documentazione del progetto è una breve dimostrazione del funzionamento dell'applicazione. Rispetto alla presentazione nel video sono state aggiunte nuove funzionaltà.

## License
[MIT](https://choosealicense.com/licenses/mit/)
