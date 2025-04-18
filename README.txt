Sam Deitch's NSL Project

The first main class contains all of the different options when choosing what to do in the NSL bank. 2,4, and 7
are the three interfaces that are working. The first interface accountChange() is the second interface which allows
you to choose which branch you would like to use teller(Bank teller) or atm. Then if you chose atm you can either check
the balance of an account or withdraw money. For a bank teller you can withdraw deposit and check the balance.
For the second interface which is number 4, openAccount(), you can create a new savings or checking account.
All you have to do is enter in a new account id and customer id and tell the program how much you want to have as a starting balance.
Next in interface 7, purchaseCard(), there is the ability to purchase with one of the cards you have, it could either be a debit
or credit card. Once you purchase with a card, it will go into the balance that is associated with that card, like for debit it 
has a balance in accounts that is related to it. So once a purchase is made, that account should have money withdrawn from it.

Run:
To run use the command java -cp ojdbc11.jar:./ NSL
If not working run:
jar cfmv Capacity.jar Manifest.txt Capacity.class  //making sure that the paths for each file are correct from where you are running the command, and add additional classes at the end of the line if need be.


Here are some customer ids that can be used to test whenever it asks for a user id:
87833761
43114726
54258091
66025982
19406377
42589771
63981219
84349663
33210233
45192563


