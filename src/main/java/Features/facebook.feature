Feature: WalletHub technical assignment

@MainTest
Scenario Outline: login to Facebook to post status

Given user is already on Facebook Web Page
When user logs in with "<username>" and "<password>"
And user post status "<message>"
Then user sees the "<message>" on Facebook page

Examples:
	| username | password | message |
	|07899517060| WalletHub@1 | Hello World	 |

