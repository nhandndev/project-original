$content = Get-Content -Path 'src\main\java\com\mycompany\Main.java' -Raw
$vars = @('sc', 'bookManager', 'memberManager', 'borrowManager')
foreach ($v in $vars) {
    $content = [regex]::Replace($content, '(?<!this\.|String |List\<[\w]+\> |int |boolean )(\b' + $v + '\.)', 'this.' + $v + '.')
}
$methods = @('run', 'printMainMenu', 'readInt', 'manageBooks', 'addBook', 'updateBook', 'removeBook', 'viewAllBooks', 'searchBooks', 'manageMembers', 'addMember', 'updateMember', 'removeMember', 'viewAllMembers', 'searchMembers', 'manageBorrowing', 'borrowBook', 'returnBook', 'viewCurrentlyBorrowedBooks', 'viewBorrowingHistoryByMember', 'manageReports', 'viewPopularBooksSimple', 'viewMembersBorrowingCount', 'viewOverdueBooks')
foreach ($m in $methods) {
    $content = [regex]::Replace($content, '(?<!this\.|void |int )(\b' + $m + '\()', 'this.' + $m + '(')
}
Set-Content -Path 'src\main\java\com\mycompany\Main.java' -Value $content
