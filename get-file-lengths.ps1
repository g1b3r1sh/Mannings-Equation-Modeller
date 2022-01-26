git ls-files '*.java' | foreach-Object -begin {$table = @{}} -process {$table[$_] = (Get-Content $_).Length} -end {Write-Host -NoNewline "Total: " ($table.GetEnumerator() | Measure-Object Value -Sum).Sum; $table.GetEnumerator()} | Sort-Object -Property Value -Descending | ft -auto
pause