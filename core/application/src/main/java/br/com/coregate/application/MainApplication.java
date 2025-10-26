package br.com.coregate.application;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainApplication {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
    }
}


//application
// ├── port
// │   ├── in
// │   │    ├── AuthorizeTransactionUseCase.java
// │   │    ├── StandInEvaluationUseCase.java
// │   │    └── AdviceGenerationUseCase.java
// │   └── out
// │        ├── TransactionRepositoryPort.java
// │        ├── TenantRepositoryPort.java
// │        ├── MerchantRepositoryPort.java
// │        ├── AdvicePublisherPort.java
// │        └── EventPublisherPort.java
// ├── service
// │   ├── AuthorizeTransactionService.java
// │   ├── StandInEvaluationService.java
// │   └── AdviceGenerationService.java
// ├── dto
// │   ├── TransactionCommand.java.java
// │   └── AuthorizationResult.java
// └── exception
//      └── BusinessException.java
