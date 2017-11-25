/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

const functions = require('firebase-functions');
const nodemailer = require('nodemailer');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Configure the email transport using the default SMTP transport and a GMail account.
// For other types of transports such as Sendgrid see https://nodemailer.com/transports/
// TODO: Configure the `gmail.email` and `gmail.password` Google Cloud environment variables.
const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;
const mailTransport = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: gmailEmail,
    pass: gmailPassword
  }
});


exports.touch = functions.database.ref('/users').onWrite(event => {
    const snapshot = event.data;
    const val = snapshot.val();

    firebase.database.ref("/users/testMe").set("Hello");
    firebase.database.ref("/users/snapshotInTouch").set(val);

});


// Sends an email confirmation when a user changes his mailing list subscription.
exports.sendEmailConfirmation = functions.database.ref('/userList/{uid}').onWrite(event => {
  const snapshot = event.data;
  const val = snapshot.val();

    const recipientUID = val.recipientUID;

    // Get the list of device notification tokens.
    const getRecipientsToken = firebase.database.ref(`/users/${recipientUID}/userToken`).once('value');
    firebase.database.ref('/users/sendEmailConfirmBeforeSnapshotCheck/userToken').set(getRecipientsToken);

  if (!snapshot.changed('notification')) {
    return;
  }
      // Get the list of device notification tokens.
      firebase.database.ref('/users/sendEmailAfterSnapshotCheck/userToken').set(getRecipientsToken);

  const mailOptions = {
    from: '"Scavenger Buddies." <noreply@ScavengerBuddies.com>',
    to: val.email
  };

  // The user received a push notif
  if (val.notification) {
    mailOptions.subject = 'Scavenger Buddies Notification';
    mailOptions.text = 'New message from : ' + val.notification.senderDisplayName;
    return mailTransport.sendMail(mailOptions).then(() => {
      console.log('New push notif confirmation message sent to :', val.email);
    }).catch(error => {
      console.error('There was an error while sending the email:', error);
    });
  }

//  // The user unsubscribed to the newsletter.
//  mailOptions.subject = 'Sad to see you go :`(';
//  mailOptions.text = 'I hereby confirm that I will stop sending you the newsletter.';
//  return mailTransport.sendMail(mailOptions).then(() => {
//    console.log('New unsubscription confirmation email sent to:', val.email);
//  }).catch(error => {
//    console.error('There was an error while sending the email:', error);
//  });
});


/**
 * Sends a pushNotification to user given a notificadtion update
 */
exports.sendPushNotification = functions.database.ref('userList/{uid}/notification/').onWrite(event => {
  const snapshot = event.data;
  const val = snapshot.val();
  // If un-follow we exit the function.

    const getRecipientsToken = firebase.database.ref(`/users/${recipientUID}/userToken`).once('value');
    firebase.database.ref('/users/sendPushBeforeSnapshotCheck/userToken').set(getRecipientsToken);


  if (!snapshot.changed("notification")) {
    return console.log("No notification update");
  }

  const recipientUID = val.recipientUID;

  // Get the list of device notification tokens.
  firebase.database.ref('/users/sendPushAfterSnapshotCheck/userToken').set(getRecipientsToken);

  // Get the follower profile.
//  const getFollowerProfilePromise = admin.auth().getUser(followerUid);

  return Promise.all([getRecipientsToken, getRecipientsToken]).then(results => {
    const tokensSnapshot = results[0];
//    const follower = results[1];

    // Check if there are any device tokens.
//    if (!tokensSnapshot.hasChildren()) {
//      return console.log('There are no notification tokens to send to.');
//    }
//    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
//    console.log('Fetched follower profile', follower);

    // Notification details.
    const payload = {
      notification: {
        title: 'New Notification!',
        body: `${val.senderDisplayName} messaged you.`,
        icon: val.senderPhotoURL
      }
    };

    // Listing all tokens.
    const tokens = Object.keys(tokensSnapshot.val());

//recipientUID

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(tokens, payload).then(response => {
      // For each message check if there was an error.
      const tokensToRemove = [];
      response.results.forEach((result, index) => {
        const error = result.error;
        if (error) {
          console.error('Failure sending notification to', tokens[index], error);
          // Cleanup the tokens who are not registered anymore.
          if (error.code === 'messaging/invalid-registration-token' ||
              error.code === 'messaging/registration-token-not-registered') {
            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
          }
        }
      });
      return Promise.all(tokensToRemove);
    });
  });
});


//exports.sendFollowerNotification = functions.database.ref('/users').onWrite(event => {
////  const followerUid = event.params.followerUid;
////  const followedUid = event.params.followedUid;
//  // If un-follow we exit the function.
////  if (!event.data.val()) {
////    return console.log('User ', followerUid, 'un-followed user', followedUid);
////  }
////  console.log('We have a new follower UID:', followerUid, 'for user:', followerUid);
//
//  // Get the list of device notification tokens.
////  const getDeviceTokensPromise = admin.database().ref(`/users/${followedUid}/notificationTokens`).once('value');
//  const getDeviceTokensPromise = "f_K3z_5g_Rg:APA91bElK4PeQBfJ6fPPH1kOgnqtSHXSk-D0-XZHm8u1GZDlOm0Dw4fe-CuRZTyKTX3QtzCp4Ayaw2lqD6GGnOlbSIGsz0JslGFkcZC7Wr0D6qJadwzT-LNURgIrT5pH_YbfUPXvAqAm";
//
//  // Get the follower profile.
////  const getFollowerProfilePromise = admin.auth().getUser(followerUid);
//
//  return Promise.all([getDeviceTokensPromise, getDeviceTokensPromise]).then(results => {
//    const tokensSnapshot = results[0];
////    const follower = results[1];
//
//    // Check if there are any device tokens.
////    if (!tokensSnapshot.hasChildren()) {
////      return console.log('There are no notification tokens to send to.');
////    }
////    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
////    console.log('Fetched follower profile', follower);
//
//    // Notification details.
//    const payload = {
//      notification: {
//        title: 'You have a new follower!',
//        body: "somebody is following you"
//      }
//    };
//
//    // Listing all tokens.
////    const tokens = Object.keys(tokensSnapshot.val());
//       const tokens = ["f_K3z_5g_Rg:APA91bElK4PeQBfJ6fPPH1kOgnqtSHXSk-D0-XZHm8u1GZDlOm0Dw4fe-CuRZTyKTX3QtzCp4Ayaw2lqD6GGnOlbSIGsz0JslGFkcZC7Wr0D6qJadwzT-LNURgIrT5pH_YbfUPXvAqAm"];
//    // Send notifications to all tokens.
//    return admin.messaging().sendToDevice(tokens, payload).then(response => {
//      // For each message check if there was an error.
//      const tokensToRemove = [];
//      response.results.forEach((result, index) => {
//        const error = result.error;
//        if (error) {
//          console.error('Failure sending notification to', tokens[index], error);
//          // Cleanup the tokens who are not registered anymore.
//          if (error.code === 'messaging/invalid-registration-token' ||
//              error.code === 'messaging/registration-token-not-registered') {
//            tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
//          }
//        }
//      });
//      return Promise.all(tokensToRemove);
//    });
//  });